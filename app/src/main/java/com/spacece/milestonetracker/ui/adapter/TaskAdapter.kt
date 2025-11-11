package com.spacece.milestonetracker.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.data.model.Task
import com.spacece.milestonetracker.databinding.ItemTaskBinding
import com.spacece.milestonetracker.utils.*

class TaskAdapter(
    private val activity: Activity,
    private val onCheckedChange: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, activity, onCheckedChange)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        val previousTask = if (position > 0) getItem(position - 1) else null
        holder.bind(task, previousTask)
    }

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val activity: Activity,
        private val onCheckedChange: (Task, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, previousTask: Task?) = with(binding) {

            // --- Title & Category ---
            tvTaskTitle.setupText(task.title)
            chipCategory.setupText(task.category)
            checkboxComplete.isChecked = task.isCompleted

            // --- Timestamp (Date) ---
            val currentDate = task.timestamp.toCalenderDate()
            tvDate.setupText(currentDate)

            // --- Hide date if same as previous task’s date ---
            if (previousTask != null &&
                previousTask.timestamp.toCalenderDate() == currentDate) {
                tvDate.gone()
            } else {
                tvDate.visible()
            }

            // --- Checkbox change listener ---
            checkboxComplete.setOnCheckedChangeListener(null)
            checkboxComplete.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(task, isChecked)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.timestamp == newItem.timestamp
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}
