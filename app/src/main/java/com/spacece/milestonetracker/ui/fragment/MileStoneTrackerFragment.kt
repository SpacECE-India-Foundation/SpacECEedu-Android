package com.spacece.milestonetracker.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.MilestoneTask
import com.spacece.milestonetracker.databinding.DialogUploadvideoBinding
import com.spacece.milestonetracker.databinding.FragmentMileStoneTrackerBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.TaskAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setVisibility
import java.io.File
import java.io.FileOutputStream

class MileStoneTrackerFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentMileStoneTrackerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter

    private var dialogBinding: DialogUploadvideoBinding? = null
    private var videoUri: Uri? = null

    private var userId: String? = null
    private var childId: String? = null

    private var lastTouchedTaskId: String? = null
    private var lastTouchedTaskWasChecked: Boolean = false

    private lateinit var loaderDialog: Dialog

    private var taskId: String = ""

    private var apiTasks : List<MilestoneTask> = emptyList()



    // Video Picker
    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            val mimeType = requireContext().contentResolver.getType(uri)

            if (mimeType?.startsWith("video/") == true) {
                videoUri = uri
                val fileName = getFileName(uri)
                dialogBinding?.etUploadLink?.setText(fileName)
                toast("Video selected: $fileName")
            } else toast("Please upload video files only.")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMileStoneTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewsAndListeners()

        // 🔥 Fetch IDs
        // 🔥 Debug
        Log.d("MILESTONE_DBG", "Calling loadMilestoneTasks user=$userId child=$childId")

        // 🔥 Load tasks

        observeMilestoneTasks()
        getMilestoneTasks()
        observeUpdateTaskStatus() // for update check box
        observeUploadLoader() // for upload loader
        observeSubmitMilestone() // for submit video task
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> (activity as ParentMainActivity).onBackPressAction()
        }
    }

//    private fun setupRecyclerView() {
//        adapter = TaskAdapter(
//            requireActivity(),
//            onCheckedChange = { task, isChecked ->
//                println("Task '${task.task}' completed: $isChecked")
//            },
//            onUploadClick = { task ->
//                showUploadDialog(task)
//            }
//        )
//
//        binding.rvMilestoneTasks.adapter = adapter
//    }

    private fun setupRecyclerView() {

        adapter = TaskAdapter(
            requireActivity(),
            onCheckedChange = { task, isChecked ->

                // Step 1: optimistic UI update
                optimisticUpdateTaskInAdapter(task, isChecked)

                // Step 2: remember last touched task for rollback
                lastTouchedTaskId = task.taskId
                lastTouchedTaskWasChecked = isChecked

                // Step 3: call ViewModel API
                milestoneTrackerViewModel.updateTaskStatus(
                    userId = userId.toString(),               // <-- make sure we find the user id
                    childId = childId.toString(),             // <-- this is static for now
                    taskId = task.taskId.toString(),
                    completed = isChecked
                )
            },

            onUploadClick = { task ->
                showUploadDialog(task)
                taskId = task.taskId.toString()
            }
        )

        binding.rvMilestoneTasks.adapter = adapter
    }

    // function for updatetaskStatusResutl ui update
    private fun optimisticUpdateTaskInAdapter(task: MilestoneTask, isChecked: Boolean) {
        val newList = adapter.currentList.toMutableList()
        val pos = newList.indexOfFirst { it.taskId == task.taskId }

        if (pos != -1) {
            val updated = newList[pos].copy(isCompleted = isChecked)
            newList[pos] = updated
            adapter.submitList(newList)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUpdateTaskStatus() {
        milestoneTrackerViewModel.updateTaskStatusResult.observe(viewLifecycleOwner) { result ->

            // null → ignore
            if (result == null) return@observe

            val success = result.contains("success", ignoreCase = true)
                    || result.contains("updated", ignoreCase = true)

            if (!success) {
                // API failed → rollback
                rollbackTaskUpdate()
                toast("Failed: $result")
            } else {
                binding.apply {
                    tvProgress.setText(
                        (tvProgress.text.toString().substringBefore("/")
                            .toInt() + 1).toString() + "/" + adapter.itemCount
                    )
                }
                toast("Task updated")
            }

            lastTouchedTaskId = null
        }
    }

    private fun rollbackTaskUpdate() {
        val taskId = lastTouchedTaskId ?: return

        val newList = adapter.currentList.toMutableList()
        val pos = newList.indexOfFirst { it.taskId == taskId }

        if (pos != -1) {
            val original = newList[pos]
            val reverted = original.copy(isCompleted = false)
            newList[pos] = reverted
            adapter.submitList(newList)
        }
    }


    private fun observeMilestoneTasks() {
        milestoneTrackerViewModel.milestoneData.observe(viewLifecycleOwner) { response ->

            Log.d(
                "MILESTONE_DBGG",
                " loadMilestoneTasks List=${response?.tasks?.size} child=$childId"
            )

            binding.progressBar.setVisibility(false)

            response?.tasks?.let { apiTasks ->

                Log.d("MILESTONE_DBGG", " loadMilestoneTasks List=${apiTasks.size} child=$childId")

                binding.apply {
                    val activityCount =
                        apiTasks.count { it.type.equals("activity", ignoreCase = true) }
                    val milestoneCount =
                        apiTasks.count { it.type.equals("milestone", ignoreCase = true) }
                    val completed = apiTasks.count { it.isCompleted == true }
                    tvMilestone.setText(milestoneCount.toString())
                    tvTasks.setText(activityCount.toString())
                    tvProgress.setText(completed.toString() + "/" + apiTasks.size.toString())
                }

                this.apiTasks = apiTasks // saved
                adapter.submitList(apiTasks)
            }
        }
    }

    private fun showUploadDialog(task: MilestoneTask) {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding = DialogUploadvideoBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding!!.root)

        dialogBinding?.apply {

            btnClose.setOnClickListener { dialog.dismiss() }
            btnCancel.setOnClickListener { dialog.dismiss() }
            btnUploadFile.setOnClickListener { pickVideoLauncher.launch("video/*") }

            btnSubmit.setOnClickListener {

                if (videoUri == null) {
                    toast("Please select a video")
                    return@setOnClickListener
                }

                val file = uriToFile(videoUri!!)
                if (file == null) {
                    toast("Could not process selected video")
                    return@setOnClickListener
                }

//                btnSubmit.isEnabled = false
//                btnUploadFile.isEnabled = false

                showLoaderDialog()  // ⬅ SHOW LOADER HERE

                milestoneTrackerViewModel.submitMilestoneTask(
                    userId = userId.toString(),
                    childId = childId.toString(),
                    taskId = task.taskId.toString(),
                    videoFile = file
                )

                dialog.dismiss()
            }


        }

        dialog.show()
    }

    private fun isValidVideoUrl(url: String): Boolean {
        val pattern =
            "(?i)(https?://)?(www\\.)?(youtube\\.com|youtu\\.be|vimeo\\.com|.*\\.mp4|.*\\.mov|.*\\.avi).*"
        return url.matches(pattern.toRegex())
    }

    private fun getFileName(uri: Uri): String? {
        requireContext().contentResolver.query(uri, null, null, null, null)?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) return it.getString(index)
            }
        }
        return null
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun getMilestoneTasks() {
        userId = sharedPrefs.getUserId()
        childId = sharedPrefs.getSelectedChildId()
        binding.progressBar.setVisibility(true)
        milestoneTrackerViewModel.loadMilestoneTasks(
            userId ?: "",
            childId ?: ""
        )
    }


    // for submit task api
    private fun uriToFile(uri: Uri): File? {
        val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return null
        val file = File(requireContext().cacheDir, "upload_video_${System.currentTimeMillis()}.mp4")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return file
    }

    @SuppressLint("SetTextI18n")
    private fun observeSubmitMilestone() {

        milestoneTrackerViewModel.submitMilestoneResult.observe(viewLifecycleOwner) { result ->

            hideLoaderDialog()  // ⬅ HIDE LOADER WHEN API COMPLETES

//            btnSubmit.isEnabled = true
//            btnUploadFile.isEnabled = true

            if (result == "Task Submitted.") {
                binding.apply {
                    tvProgress.setText(
                        (tvProgress.text.toString().substringBefore("/")
                            .toInt() + 1).toString() + "/" + adapter.itemCount
                    )
                }
                showSuccessDialog()

                apiTasks = apiTasks.map { task ->
                    if (task.taskId == taskId) {
                        task.copy(isCompleted = true)
                    } else {
                        task
                    }
                }

                adapter.submitList(apiTasks)

            } else {
                toast(result ?: "upload failed")
            }
        }

    }


    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val view = layoutInflater.inflate(R.layout.dialog_success, null)
        dialog.setContentView(view)

        dialog.show()

        // Auto dismiss after 1.2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 1200)
    }

    private fun observeUploadLoader() {
        milestoneTrackerViewModel.isUploading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    private fun showLoaderDialog() {
        loaderDialog = Dialog(requireContext())
        loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loaderDialog.setCancelable(false)
        loaderDialog.setContentView(R.layout.loader_dialog)
        loaderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loaderDialog.show()
    }

    private fun hideLoaderDialog() {
        if (loaderDialog.isShowing) loaderDialog.dismiss()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getMilestoneTasks()
        } else {
            binding.apply {
                tvMilestone.setText("0")
                tvTasks.setText("0")
                tvProgress.setText("0/0")
            }
            adapter.submitList(null)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
