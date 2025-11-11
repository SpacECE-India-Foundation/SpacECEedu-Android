package com.spacece.milestonetracker.data.model

data class Task(
    val title: String,
    val category: String,
    val isCompleted: Boolean,
    val timestamp: Long = 0,
)
