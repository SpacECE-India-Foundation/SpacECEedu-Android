package com.spacece.milestonetracker.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.local.getCurrentUserId
import com.spacece.milestonetracker.data.model.Child
import com.spacece.milestonetracker.data.model.ChildKaDetails
import com.spacece.milestonetracker.data.model.HeightProgress
import com.spacece.milestonetracker.data.model.WeightProgress
import com.spacece.milestonetracker.data.remote.STATUS_CODE_SUCCESS
import com.spacece.milestonetracker.databinding.FragmentHomeBinding
import com.spacece.milestonetracker.ui.activity.LoginActivity
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.ChildrenAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.UsefulFunctions
import com.spacece.milestonetracker.utils.setButtonProgress
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setVisibility
import com.spacece.milestonetracker.utils.setupText
import com.spacece.milestonetracker.utils.startActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HomeFragment : BaseFragment(), OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChildrenAdapter
    private var childrenList: List<Child> = emptyList()
    private lateinit var profile: ChildKaDetails

    private var userId: Int? = null

    companion object {
        var isNewChildAdded = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners(listOf(binding.llMilestoneTracker, binding.tvAddChild))
        viewModelObservers()
        binding.apply {
            llNoChild.setVisibility(false)
            llChildDetails.setVisibility(false)
            progressBar.setVisibility(true)
            imgProfile.clipToOutline = true
        }
        lifecycleScope.launch {
            userId = getCurrentUserId(requireContext())
            sharedPrefs.saveUserId(userId.toString())   // 🔥 ADD THIS
            userId?.let { id ->
                if (sharedPrefs.isUserLoggedIn()) {
                    Log.d("UI", "$userId")
                    Log.d("F", "Fetching")
                    milestoneTrackerViewModel.fetchAllChildren(id)
                }
            }
            if (userId == null || !sharedPrefs.isUserLoggedIn()) {
                binding.apply {
                    llNoChild.setVisibility(true)
                    llChildDetails.setVisibility(false)
                    progressBar.setVisibility(false)
                }
            }
        }
        binding.btnSubmit.setButtonProgress(binding.progressBarSubmit, false)
        setupChildrenRecyclerView()
    }

    private fun setupFragmentDataView() = with(binding) {
        val isDataView = sharedPrefs.isUserLoggedIn() && childrenList.isNotEmpty()
        llNoChild.setVisibility(!isDataView)
        llChildDetails.setVisibility(isDataView)
        progressBar.setVisibility(false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_milestoneTracker -> openFragment(R.string.text_milestone_tracker)
            R.id.tv_add_child -> if (sharedPrefs.isUserLoggedIn()) {
                openFragment(R.string.text_add_child)
            } else {
                activity?.startActivity(LoginActivity::class.java)
            }
        }
    }

    private fun openFragment(tabOrFragId: Int) {
        (activity as? ParentMainActivity)?.replaceFragment(tabOrFragId)
    }

    private fun setupLineChart(lineChart: LineChart, entries: List<Entry>) {
        /*val entries = listOf(
            Entry(0f, 12f),
            Entry(1f, 28f),
            Entry(2f, 20f),
            Entry(3f, 32f),
            Entry(4f, 40f)
        )*/

        val dataSet = LineDataSet(entries, "").apply {
            color = Color.BLACK
            lineWidth = 2f
            setCircleColor(Color.BLACK)
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextSize = 10f
            valueTextColor = Color.BLACK
            setDrawFilled(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        lineChart.data = LineData(dataSet)

        val months = listOf("April", "May", "June", "July", "August")
        val xAxis = lineChart.xAxis
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(getMonthLabels(entries.size))
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = Color.BLACK
            textSize = 12f
            granularity = 1f
        }

        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.apply {
            textColor = Color.BLACK
            axisMinimum = 0f
        }

        lineChart.apply {
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            setDrawGridBackground(false)
            setTouchEnabled(false)
            invalidate()
        }
    }

    /**
     * Sets up the horizontal RecyclerView with children and an "Add Child" button
     */
    // this version mocks API data with static sample children.
    // when our backend is fixed, we can easily replace this with our API call:
    //viewModel.fetchAllChildren(userId)

    private fun setupChildrenRecyclerView() {
        adapter = ChildrenAdapter(
            childrenList,
            onChildClick = { child ->

                // save the child id in shared prefrenses so we can use it in milestone task
                sharedPrefs.saveSelectedChildId(child.childId.toString())

                //showChildDetails(child)
                binding.progressBarChild.setVisibility(true)
                binding.llMilestoneDetails.setVisibility(false)
                showProfileData(child.childId)
            },
            onAddClick = {
                showAddChildDialog()
            }
        )



        binding.rvChildren.apply {
            adapter = this@HomeFragment.adapter
            setHasFixedSize(true)
        }
    }
    /*private fun showChildDetails(child: Child) {
        // Make sure the layout is visible
        binding.layoutChildDetails.setVisibility(true)

        // Load profile image using Glide
        Glide.with(requireContext())
            .load(child.image ?: "https://static.vecteezy.com/system/resources/previews/007/312/854/large_2x/child-profile-sketch-vector.jpg") // default if null
            .into(binding.imgProfile)

        // Set text details
        binding.tvName.text = child.childName ?: "Unknown"

        val age = UsefulFunctions.DateFunc.calculateAgeFromDob(child.dob)
        binding.tvAge.setText(age.toString() + " Years")


        binding.tvAge.text = "${child.dob ?: "--"} Years"
        binding.tvLocation.text = child.center ?: "N/A"
        binding.tvHeightValue.text = "${child.height ?: "--"} cm"
        binding.tvWeightValue.text = "${child.weight ?: "--"} kg"

        // Optionally hide “update” notice if not needed
        binding.tvNotice.visibility = View.GONE

        // Add listener for the Submit button
        binding.btnSubmit.setOnClickListener {
            val newHeight = binding.heightInput.text.toString()
            val newWeight = binding.weightInput.text.toString()

            if (newHeight.isNotEmpty() && newWeight.isNotEmpty()) {
                Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT).show()
                setupChildGrowthUpdate(child.childId)
            } else {
                Toast.makeText(requireContext(), "Please enter both height and weight", Toast.LENGTH_SHORT).show()
            }
        }
    }*/


    private fun showAddChildDialog() {

        openFragment(R.string.text_add_child)
        /*val newChild = Child(
            childId = (childrenList.size + 1),
            childName = "New Child ${childrenList.size + 1}",
            dob = "01/01/2025",
            gender = "Unknown",
            center = "Test Center",
            image = null,
            height = 0,
            weight = 0
        )

        childrenList.add(newChild)
        adapter.updateList(childrenList)
        binding.rvChildren.smoothScrollToPosition(childrenList.size - 1)

        Toast.makeText(requireContext(), "${newChild.childName} added!", Toast.LENGTH_SHORT).show()*/
    }


    private fun setupChildGrowthUpdate(childId: Int) {
        binding.btnSubmit.setOnClickListener {
            val heightInput = binding.heightInput.text.toString().trim()
            val weightInput = binding.weightInput.text.toString().trim()

            if (heightInput.isEmpty() || weightInput.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter both height and weight",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            userId?.let { id ->
                binding.btnSubmit.setButtonProgress(binding.progressBarSubmit, true)
                milestoneTrackerViewModel.updateChildGrowth(id, childId, heightInput, weightInput)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong, Couldn't get User ID",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun showProfileData(childId: Int) {
        userId?.let { id ->
            milestoneTrackerViewModel.getChildDetails(id, childId)
        }
    }

    private fun viewModelObservers() {
        // For profile initialization
        milestoneTrackerViewModel.childDetailsResponse.observe(viewLifecycleOwner) { response ->
            response.getContentIfNotHandled()?.let {
                val result = it.getOrNull()
                if (result?.status == STATUS_CODE_SUCCESS.toString()) {
                    result.data?.let { data ->
                        profile = data
                        binding.progressBarChild.setVisibility(false)
                        binding.llMilestoneDetails.setVisibility(true)
                        updateProfileUI(data)
                        setupCharts(data)
                        setupChildGrowthUpdate(data.childId)
                        updateDevelopmentProgress(data)

                    } ?: run {
                        Toast.makeText(requireContext(), "Profile data is null", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        result?.message ?: "Unable to get child details.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // For childrenList initialization
        milestoneTrackerViewModel.childrenResponse.observe(viewLifecycleOwner) { response ->
            response.getContentIfNotHandled()?.let {
                val result = it.getOrNull()
                if (result?.status == STATUS_CODE_SUCCESS.toString()) {
                    childrenList = result.data?.children ?: emptyList()
                    adapter.updateList(childrenList)
                } else {
                    Toast.makeText(
                        requireContext(),
                        result?.message ?: "Something went wrong, Unable to fetch data!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setupFragmentDataView()
            }
        }

        milestoneTrackerViewModel.updateChildGrowthResponse.observe(viewLifecycleOwner) { event ->
            binding.btnSubmit.setButtonProgress(binding.progressBarSubmit, false)
            event.getContentIfNotHandled()?.let { result ->
                result.onSuccess { response ->
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Child growth updated!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val heightInput = binding.heightInput.text.toString().trim()
                    val weightInput = binding.weightInput.text.toString().trim()
                    // Optionally update UI instantly
                    binding.tvHeightValue.text = "$heightInput cm"
                    binding.tvWeightValue.text = "$weightInput kg"

                    // Clear input fields
                    binding.heightInput.text?.clear()
                    binding.weightInput.text?.clear()
                }.onFailure { e ->
                    Toast.makeText(requireContext(), "Failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun updateProfileUI(profileData: ChildKaDetails) {
        binding.apply {
            tvName.text = profileData.childName
            tvGender.text = "(" + profileData.gender + ")"
            val age = UsefulFunctions.DateFunc.calculateAgeFromDob(profileData.dob?.trim())
            tvAge.text = "$age Years"

//            tvAge.text = profileData.dob
            tvLocation.text = profileData.center
            tvHeightValue.text = "${profileData.height} cm"
            tvWeightValue.text = "${profileData.weight} kg"

            val lastHeight = profileData.heightProgress.lastOrNull()?.height
            //tvLastCheck.text = "${lastHeight ?: profileData.height} cm"
            //tvNextCheck.text = "Today"
            //tvNotice.text = "Today is your child's physical data update"

            val imageUrl =
                "https://hustle-7c68d043.mileswebhosting.com/spacece/" + profileData.image

            Glide.with(imgProfile.context)
                .load(imageUrl)
                .placeholder(
                    if (profileData.gender.equals(
                            "male",
                            true
                        )
                    ) R.drawable.boy else R.drawable.girl
                )
                .into(imgProfile)
        }
    }


    private fun convertHeightToChartEntries(data: List<HeightProgress>): List<Entry> {
        return data.mapIndexed { index, data ->
            val xValue = index.toFloat()
            val yValue = data.height.toFloat()
            Entry(xValue, yValue)
        }
    }

    private fun convertWeightToChartEntries(data: List<WeightProgress>): List<Entry> {
        return data.mapIndexed { index, data ->
            val xValue = index.toFloat()
            val yValue = data.weight.toFloat()
            Entry(xValue, yValue)
        }
    }

    private fun setupCharts(profileData: ChildKaDetails) {
        setupLineChart(binding.lineChart, convertHeightToChartEntries(profileData.heightProgress))
        setupLineChart(binding.lineChart1, convertWeightToChartEntries(profileData.weightProgress))
    }

    private fun getMonthLabels(dataSize: Int): List<String> {
        val calendar = Calendar.getInstance()
        return (0 until dataSize).map {
            calendar.add(Calendar.MONTH, -1)
            SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
        }.reversed()
    }


    // Sets a map of progressBar value and Status text
    private fun answersForProgress(catId: String): Map<String, String> {
        val answers = profile.childProgress.find { it.catId == catId }
        if (answers == null) {
            Log.w("AnswersForProgress", "⚠️ No answers found for catId: $catId")
            return mapOf("-1" to "No Data")
        }

        val ans = answers.ans
        val q1 = ans.q1.trim()
        val q2 = ans.q2.trim()
        val q3 = ans.q3.trim()

        // Count how many "Yes" answers (1) the child has
        val yesCount = listOf(q1, q2, q3).count { it == "1" }

        val result = when (yesCount) {
            0 -> mapOf("0" to "Poor")        // No "Yes" answers (all answered "No")
            1 -> mapOf("1" to "Developing")  // One "Yes" answer
            2 -> mapOf("2" to "Good")        // Two "Yes" answers
            3 -> mapOf("3" to "Perfect")     // All three "Yes" answers
            else -> {
                Log.w("AnswersForProgress", "⚠️ Invalid yes count: $yesCount")
                mapOf("-1" to "Unknown")
            }
        }
        return result
    }

    private fun initializeProgressBars() = with(binding) {
        // Set max values
        progressLanguage.max = 3
        progressMotor.max = 3
        progressCognitive.max = 3
        progressSocial.max = 3

        // Reset all to 0
        progressLanguage.progress = 0
        progressMotor.progress = 0
        progressCognitive.progress = 0
        progressSocial.progress = 0

        // Reset all status texts
        val status = "Unavailable"
        textLanguageStatus.text = status
        textMotorStatus.text = status
        textCognitiveStatus.text = status
        textSocialStatus.text = status
    }

    private fun updateDevelopmentProgress(profileData: ChildKaDetails) {
        var poorSkillFound = false
        var poorSkillCategory = ""
        initializeProgressBars()
        profileData.childProgress.forEach { childProgress ->
            val catId = childProgress.catId

            val progressMap = answersForProgress(catId)
            val progressValue = progressMap.entries.firstOrNull()?.key ?: "-1"
            val statusText = progressMap.entries.firstOrNull()?.value ?: "N/A"

            setProgressBarValue(catId, progressValue.toIntOrNull() ?: 0)
            setStatusText(catId, statusText)

            // Checks for poor skills
            if (progressValue == "0" && !poorSkillFound) {
                poorSkillFound = true
                poorSkillCategory = catId
                Log.d("UpdateDevProgress", "⚠️ Poor skill found in catId: $catId")
            }
        }

        setObserverNote(poorSkillCategory, poorSkillFound)
    }

    private fun setProgressBarValue(catId: String, progressValue: Int) = with(binding) {
        when (catId) {
            "1" -> progressLanguage.progress = progressValue
            "2" -> progressMotor.progress = progressValue
            "3" -> progressCognitive.progress = progressValue
            "4" -> progressSocial.progress = progressValue
            else -> Log.w("SetProgress", "Unknown catId: $catId")
        }
    }

    private fun setStatusText(catId: String, statusText: String) = with(binding) {
        when (catId) {
            "1" -> textLanguageStatus.setupText(statusText)
            "2" -> textMotorStatus.setupText(statusText)
            "3" -> textCognitiveStatus.setupText(statusText)
            "4" -> textSocialStatus.setupText(statusText)
            else -> Log.w("SetStatus", "Unknown catId: $catId")
        }
    }

    private fun setObserverNote(catId: String, hasPoorProgress: Boolean) {
        binding.textObserverNote.setupText(
            when {
                !hasPoorProgress -> {
                    "\"${profile.childName}'s is going good, keep it up!\". Keep focusing on your child!\""
                }

                catId == "1" -> {
                    "\"${profile.childName} should focus on developing Language skills\""
                }

                catId == "2" -> {
                    "\"${profile.childName} should focus on developing Motor skills\""
                }

                catId == "3" -> {
                    "\"${profile.childName} should focus on developing Cognitive skills\""
                }

                catId == "4" -> {
                    "\"${profile.childName} should focus on developing Social skills\""
                }

                else -> {
                    "\"Keep monitoring ${profile.childName}'s development progress\""
                }
            }
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            if (sharedPrefs.isUserLoggedIn()) {
                if (isNewChildAdded || childrenList.isEmpty()) {
                    isNewChildAdded = false
                    binding.apply {
                        llNoChild.setVisibility(false)
                        llChildDetails.setVisibility(false)
                        progressBar.setVisibility(true)
                    }
                    milestoneTrackerViewModel.fetchAllChildren(id)
                }
            }
        }
        super.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
