package com.spacece.milestonetracker.ui.fragment

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.spacece.milestonetracker.databinding.FragmentAddChildBinding
import com.spacece.milestonetracker.ui.adapter.ChildDetailQuestionsAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.AnswerReq
import com.spacece.milestonetracker.data.model.AnswersList
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.model.CategoryList
import com.spacece.milestonetracker.data.model.Category
import com.spacece.milestonetracker.data.model.ChildDetails
import com.spacece.milestonetracker.data.model.ChildDetailsRes
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.utils.setVisibility
import com.spacece.milestonetracker.utils.setupText
import com.spacece.milestonetracker.viewModel.MilestoneTrackerViewModel
import com.spacece.milestonetracker.viewModel.vmHelper.getViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddChildFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentAddChildBinding
    private lateinit var childDetailQuestionsAdapter: ChildDetailQuestionsAdapter
    private lateinit var childName: String
    private lateinit var childGender: String
    private lateinit var center: String
    private var childId: Int? = null
    private var questionSeriesId: String? = null
    private lateinit var questionList: CategoryList

    private var imageName: String? = null
    private var uri: Uri? = null

    private var childDob: Long = 0L

    // Persist category and answers
    private val answers = mutableMapOf<String, String>()
    private val category = mutableListOf<AnswerReq>()

    private var maxSteps = 1
    private var step = 0

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            // user cancelled
            return@registerForActivityResult
        } else {
            this.uri = uri
            val fileName =
                getFileName(requireContext(), uri)
            imageName = fileName
            binding.tvUploadChildPicture.setupText(imageName ?: "Picture Selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        milestoneTrackerViewModel = getViewModel { MilestoneTrackerViewModel(requireContext()) }
        binding = FragmentAddChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack, bBack, bNext, tvChildDob, bUploadChildPicture, bCenter))
        childDetailQuestionsAdapter =
            ChildDetailQuestionsAdapter(requireActivity(), ::adapterOnChecked)
        rvQuestions.adapter = childDetailQuestionsAdapter
        setupData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.b_upload_child_picture -> {
                pickImageLauncher.launch("image/*")
            }

            R.id.b_back -> {
                onBackClick()
            }

            R.id.b_next -> {
                onNextClick()
            }

            R.id.tv_child_dob -> {
                showDatePickerDialog(binding.tvChildDob)
            }

            R.id.b_center -> {
                binding.spinnerCenter.performClick()
            }
        }
    }


    private fun adapterOnChecked(questionId: String, answer: String?) {
        if (step > 0 && step <= questionList.categories.size) {
            val currentCategory = questionList.categories[step - 1]
            // val currentQuestion = currentCategory.questions["q$position"]
            if (answer != null) {
                answers[questionId] = answer
            } else {
                answers.remove(questionId)
            }
            nextButtonState(currentCategory.que, answers)
        }
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (step != 0) {
                if (step > 0 && step <= questionList.categories.size && answers.isNotEmpty()) {
                    answerSave(questionList.categories[step - 1], answers)
                }
                step = step - 1
                setupData()
            } else {
                isEnabled = false
                (activity as ParentMainActivity).onBackPressAction()
                isEnabled = true
            }
        }
    }

    // Manages UI
    private fun setupData() {
        binding.apply {
            llAddChildForm.setVisibility(false)
            rvQuestions.setVisibility(false)
            bBack.setVisibility(false)
            pbSubmit.setVisibility(false)
            bNext.setVisibility(true)
            pbAddChild.max = 5
            tvStepNumber.setupText("${step + 1} of $maxSteps")
        }
        when (step) {
            0 -> {
                binding.apply {
                    questionList = CategoryList(emptyList())
                    childDetailQuestionsAdapter.submitList(emptyList())
                    llAddChildForm.setVisibility(true)
                    tvStepTitle.setupText("Personal Details")
                    bNext.isEnabled = true
                }
            }

            else -> {
                binding.pbAddChild.max = maxSteps
                if (!::questionList.isInitialized) {
                    Log.e("AD", "Questions not initialized yet")
                    return
                }
                if (questionList.categories.isEmpty()) {
                    Log.e("AD", "Categories list is empty!")
                    Toast.makeText(requireContext(), "No categories loaded", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (step < 1 || step > questionList.categories.size) {
                    Log.e("AD", "Invalid step: $step, categories: ${questionList.categories.size}")
                    return
                }
                val currentCategoryId = questionList.categories[step - 1].catId
                val currentQuestions = questionList.categories[step - 1].que
                answers.clear()
                val savedCategory = category.firstOrNull { it.catId == currentCategoryId }
                if (savedCategory != null) {
                    answers.putAll(savedCategory.ans)
                    Log.d(
                        "setupData",
                        "Restored ${answers.size} answers for category $currentCategoryId: $answers"
                    )
                } else {
                    Log.d("setupData", "No saved answers for category $currentCategoryId")
                }
                binding.apply {
                    tvStepTitle.setupText(questionList.categories[step - 1].catName)
                    rvQuestions.setVisibility(true)
                    bBack.setVisibility(step != questionList.categories.size)
                }
                val questionsList = currentQuestions.map { (key, value) -> key to value }
                childDetailQuestionsAdapter.submitList(questionsList)
                childDetailQuestionsAdapter.updateSavedAnswers(answers)
                nextButtonState(currentQuestions, answers)
                if (step == questionList.categories.size) {
                    binding.bNext.setupText("Submit")
                } else {
                    binding.bNext.setupText("Next>")
                }
            }
        }
        binding.pbAddChild.progress = step + 1
        binding.tvStepNumber.setupText("${step + 1} of $maxSteps")
    }

    // Submits Personal Details
    private fun detailsSubmit() {
        childName = binding.edtChildName.text.toString().trim()
        childGender = when (binding.rgChildGender.checkedRadioButtonId) {
            binding.rbMale.id -> "Male"
            binding.rbFemale.id -> "Female"
            else -> ""
        }
        center = binding.spinnerCenter.selectedItem?.toString()
            ?.takeIf { binding.spinnerCenter.selectedItemPosition > 0 }
            ?: "No Center Selected"
        val imagePart = if (uri != null && imageName != null) {
            imageToMultiPart(requireContext(), uri!!, imageName!!)
        } else {
            null
        }
        milestoneTrackerViewModel.submitChildDetails(
            imagePart,
            ChildDetails(childName, childDob, childGender, center)
        )
        milestoneTrackerViewModel.addChildResponse.observe(viewLifecycleOwner) { response ->
            response.getContentIfNotHandled()?.let(::addChildResponseObserver)
        }
    }

    // Submits Child Progress Answers
    private fun updateChildProgress() {
        showLoader(true)
        binding.bNext.setVisibility(false)
        binding.pbSubmit.setVisibility(true)
        answerSave(questionList.categories[questionList.categories.size - 1], answers)
        // Logs for questionList data check
        Log.d("UpdateProgress", "childId: $childId")
        Log.d("UpdateProgress", "questionSeriesId: $questionSeriesId")
        Log.d("UpdateProgress", "category size: ${category.size}")
        Log.d("UpdateProgress", "questionList size: ${questionList.categories.size}")
        // Log the actual answers
        category.forEachIndexed { index, answerReq ->
            Log.d(
                "UpdateProgress",
                "Category $index: ${answerReq.catName}, Answers: ${answerReq.ans}"
            )
        }
        if (childId == null) {
            restoreSubmissionUi()
            Toast.makeText(requireContext(), "Child ID not found.", Toast.LENGTH_LONG).show()
            Log.e("UpdateProgress", "childId is null")
            return
        }
        if (questionSeriesId.isNullOrBlank()) {
            restoreSubmissionUi()
            Toast.makeText(requireContext(), "Question series ID not found.", Toast.LENGTH_LONG)
                .show()
            Log.e("UpdateProgress", "questionSeriesId is null or blank")
            return
        }
        if (category.size != questionList.categories.size) {
            restoreSubmissionUi()
            Toast.makeText(
                requireContext(),
                "Please answer all categories before submitting.",
                Toast.LENGTH_LONG
            ).show()
            Log.e("UpdateProgress", "Categories: ${category.size}/${questionList.categories.size}")
            return
        }
        val questionIdInt = questionSeriesId!!.toIntOrNull()
        if (questionIdInt == null) {
            restoreSubmissionUi()
            Toast.makeText(requireContext(), "Invalid question ID format.", Toast.LENGTH_LONG)
                .show()
            return
        }
        val answersList = AnswersList(
            childId = childId!!,
            questionId = questionSeriesId!!.toInt(),
            answers = category
        )
        Log.d("UpdateProgress", "Submitting: $answersList")
        milestoneTrackerViewModel.updateChildProgress(answersList)
        milestoneTrackerViewModel.updateProgressResponse.observe(viewLifecycleOwner) { response ->
            response.getContentIfNotHandled()?.let(::updateProgressResponseObserver)
        }
    }

    // Manages Next button
    private fun onNextClick() {
        // Step 0: Submit personal details
        if (step == 0) {
            if (!detailsInputValidation()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all details!",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            showLoader(true)
            detailsSubmit()
            return
        }

        if (answers.size < questionList.categories[step - 1].que.size) {
            Toast.makeText(
                requireContext(),
                "Please answer all the queries!",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Steps 1 to second-last: Save current answers and proceed
        if (step < questionList.categories.size) {
            answerSave(questionList.categories[step - 1], answers)

            answers.clear()
            step++
            setupData()
            return
        }
        // Last step: Submit all answers
        if (step == questionList.categories.size) {
            answerSave(questionList.categories[step - 1], answers)
            updateChildProgress()
        }
    }

    // Manages Back button
    private fun onBackClick() {
        if (step > 0 && step <= questionList.categories.size && answers.isNotEmpty()) {
            answerSave(questionList.categories[step - 1], answers)
        }
        step = step - 1
        setupData()
    }

    private fun showLoader(state: Boolean) {
        if (state) {
            binding.apply {
                bNext.setVisibility(false)
                bBack.setVisibility(false)
                pbSubmit.setVisibility(true)
            }
        } else {
            binding.apply {
                bNext.setVisibility(true)
                bBack.setVisibility(step > 0)
                pbSubmit.setVisibility(false)
            }
        }
    }

    private fun restoreSubmissionUi() {
        binding.apply {
            bNext.setVisibility(true)
            pbSubmit.setVisibility(false)
        }
    }

    private fun nextButtonState(
        currentQuestions: Map<String, String>,
        currentAnswers: Map<String, String>
    ) {
        val allAnswered = currentQuestions.keys.all { q ->
            currentAnswers.contains(q)
        }
        //binding.bNext.isEnabled = allAnswered
    }

    private fun answerSave(currentCategory: Category, currentAnswers: Map<String, String>) {
        // Remove any previous entry for this category
        category.removeAll { it.catId == currentCategory.catId }
        category.add(
            AnswerReq(
                currentCategory.catId,
                currentCategory.catName,
                currentAnswers.toMap()
            )
        )
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val cursor =
            contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    return it.getString(nameIndex)
                }
            }
        }
        return null
    }

    private fun showDatePickerDialog(textView: TextView) {
        // Get current date to set as default in the dialog
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textView.text = dateFormat.format(calendar.time)
                val timestamp: Long = calendar.timeInMillis // Store the Long timestamp
                childDob = timestamp
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        // Show the dialog
        datePickerDialog.show()
    }

    private fun imageToMultiPart(
        context: Context,
        uri: Uri,
        fileName: String?
    ): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val requestBody = inputStream.readBytes()
            .toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", fileName, requestBody)
    }

    private fun detailsInputValidation(): Boolean {
        val name = binding.edtChildName.text.toString().trim()
        val gender = when (binding.rgChildGender.checkedRadioButtonId) {
            binding.rbMale.id, binding.rbFemale.id -> true
            else -> false
        }
        val centerSelected = binding.spinnerCenter.selectedItemPosition > 0
        val dobValid = childDob != 0L
        return uri != null && name.isNotBlank() && gender && centerSelected && dobValid
    }

    private fun addChildResponseObserver(result: Result<ApiResponse<ChildDetailsRes>>) {
        val data = result.getOrNull()
        val isSuccess = data != null && (
                data.status?.toInt() in 200..299 ||
                        data.message?.contains("success", ignoreCase = true) == true
                )
        if (isSuccess) {
            Log.d("QS", "Child Added successfully.")
            Log.d("RES", "API Response: ${data.message}")
            /*Toast.makeText(
                requireContext(),
                data.message ?: "Child Added Successfully.",
                Toast.LENGTH_SHORT
            ).show()*/
            HomeFragment.isNewChildAdded = true
            childId = data.data?.childId.let { id ->
                id?.let { if (it > 0) id else null }
            }
            questionSeriesId = data.data?.questionsId.let { id ->
                if (id?.isNotBlank() ?: false) id else null
            }
            Log.d("AddChild", "childId: $childId")
            Log.d("AddChild", "questionSeriesId: $questionSeriesId")
            val categoriesList = data.data?.questions ?: emptyList()
            if (categoriesList.isEmpty()) {
                showLoader(false)
                Log.e("AddChild", "No categories in response!")
                Toast.makeText(
                    requireContext(),
                    "Please try later, Currently unavailable!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            questionList = CategoryList(categoriesList)
            Log.d("AddChild", "Loaded ${questionList.categories.size} categories")
            maxSteps = questionList.categories.size + 1
            step = 1
            showLoader(false)
            setupData()
        } else {
            showLoader(false)
            val errorMsg = data?.message ?: "Something went wrong!"
            Log.e("AddChild", "Error: $errorMsg")
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgressResponseObserver(data: Result<ApiResponse<Unit>>) {
        val result = data.getOrNull()
        val isSuccess = result != null && (
                result.status?.toInt() in 200..299 ||
                        result.message?.contains("success", ignoreCase = true) == true
                )
        if (isSuccess) {
            Toast.makeText(
                requireContext(),
                "Child has been successfully added!",
                Toast.LENGTH_SHORT
            ).show()
            //If task is done call below line to exit from fragment
            (activity as ParentMainActivity).onBackPressAction()
        } else {
            restoreSubmissionUi()
            Toast.makeText(
                requireContext(),
                "Something Went Wrong, Try Again!.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            //clear defaults
            step = 0
            binding.apply {
                childName = ""
                childGender = ""
                center = ""
                childId = null
                questionSeriesId = null
                imageName = ""
                uri = null
                childDob = 0
                answers.clear()
                category.clear()
                questionList = CategoryList(emptyList())
                childDetailQuestionsAdapter.submitList(emptyList())
                tvUploadChildPicture.setupText("")
                edtChildName.setupText("")
                tvChildDob.setupText("")
                rgChildGender.clearCheck()
                spinnerCenter.setSelection(0)
            }
            setupData()
        }
        super.onHiddenChanged(hidden)
    }
}
