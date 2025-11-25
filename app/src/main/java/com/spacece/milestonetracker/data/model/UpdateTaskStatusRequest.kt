package com.spacece.milestonetracker.data.model

data class UpdateTaskStatusRequest(
    val taskId: String,
    val completed: String
)