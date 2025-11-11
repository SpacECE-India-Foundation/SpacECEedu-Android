package com.spacece.milestonetracker.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Task
import com.spacece.milestonetracker.databinding.FragmentMileStoneTrackerBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.TaskAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners

class MileStoneTrackerFragment : BaseFragment(), OnClickListener  {
    private var _binding: FragmentMileStoneTrackerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMileStoneTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter
        adapter = TaskAdapter(requireActivity()) { task, isChecked ->
            // Handle checkbox toggle
            println("Task '${task.title}' marked completed: $isChecked")
        }

        binding.rvMilestoneTasks.adapter = adapter

        // Load sample data
        val sampleTasks = getSampleTasks()

        // Submit to adapter
        adapter.submitList(sampleTasks)

        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }
        }
    }

    private fun getSampleTasks(): List<Task> {
        val currentTime = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L

        return listOf(
            Task(
                title = "Practice saying 'ball' 10 times",
                category = "Language",
                isCompleted = false,
                timestamp = currentTime
            ),
            Task(
                title = "Color inside the lines of a drawing",
                category = "Motor Skills",
                isCompleted = true,
                timestamp = currentTime
            ),
            Task(
                title = "Identify 5 fruits by name",
                category = "Cognitive",
                isCompleted = false,
                timestamp = currentTime - oneDay // Yesterday
            ),
            Task(
                title = "Read a story for 5 minutes",
                category = "Language",
                isCompleted = false,
                timestamp = currentTime - oneDay
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
