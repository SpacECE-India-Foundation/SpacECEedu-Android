package com.spacece.milestonetracker.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.data.model.MilestoneTask
import com.spacece.milestonetracker.databinding.ItemMilestoneTaskBinding
import com.spacece.milestonetracker.databinding.ItemTaskBinding
import com.spacece.milestonetracker.utils.*

class TaskAdapter(
    private val activity: Activity,
    private val onCheckedChange: (MilestoneTask, Boolean) -> Unit,
    private val onUploadClick: (MilestoneTask) -> Unit
) : ListAdapter<MilestoneTask, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_SPECIAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        val task = getItem(position)
        return if (task.type.equals("milestone", ignoreCase = true)) {
            TYPE_SPECIAL
        } else {
            TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SPECIAL) {
            val binding = ItemMilestoneTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MilestoneViewHolder(binding, onUploadClick)
        } else {
            val binding = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            TaskViewHolder(binding, activity, onCheckedChange)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = getItem(position)
        val previous = if (position > 0) getItem(position - 1) else null

        when (holder) {
            is TaskViewHolder -> holder.bind(task, previous)
            is MilestoneViewHolder -> holder.bind(task, previous)
        }
    }

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val activity: Activity,
        private val onCheckedChange: (MilestoneTask, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: MilestoneTask, previousTask: MilestoneTask?) = with(binding) {
            tvTaskTitle.setupText(task.task)
            chipCategory.setupText(task.category)
            checkboxComplete.isChecked = task.isCompleted == true
            checkboxComplete.isEnabled = task.isCompleted == false

            val currentDate = task.date
            tvDate.setupText(currentDate)

            if (previousTask?.date == currentDate) {
                tvDate.gone()
            } else tvDate.visible()

//            checkboxComplete.setOnClickListener {
//                onCheckedChange(task, true)
//                checkboxComplete.isEnabled = false
//            }

            checkboxComplete.setOnClickListener {
                // disable to prevent double click while request is in-flight
                checkboxComplete.isEnabled = false
                onCheckedChange(task, true)
            }
        }
    }

    class MilestoneViewHolder(
        private val binding: ItemMilestoneTaskBinding,
        private val onUploadClick: (MilestoneTask) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: MilestoneTask, previousTask: MilestoneTask?) = with(binding) {
            taskCard2TitleB.text = task.task
            taskCard2TagB.text = task.category

            val currentDate = task.date
            tvDate.setupText(currentDate)

            if (previousTask?.date == currentDate) {
                tvDate.gone()
            } else tvDate.visible()

            taskCard2ActionB.setVisibility(task.isCompleted == false)
            checkboxComplete.setVisibility(task.isCompleted == true)

            taskCard2ActionB.setOnClickListener {
                onUploadClick(task)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<MilestoneTask>() {

        override fun areItemsTheSame(oldItem: MilestoneTask, newItem: MilestoneTask): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MilestoneTask, newItem: MilestoneTask): Boolean {
            return oldItem == newItem
        }
    }

}
