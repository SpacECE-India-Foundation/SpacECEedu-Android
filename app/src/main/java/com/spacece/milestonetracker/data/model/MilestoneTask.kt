package com.spacece.milestonetracker.data.model


data class MilestoneTaskResponse(
    val childId: Int?,
    val milestones: String?,
    val activities: String?,
    val tasks: List<MilestoneTask>?
)

data class MilestoneTask(
    val date: String?,
    val taskId: String?,
    val type: String?,
    val category: String?,
    val task: String?,
    val isCompleted: Boolean?
)

