package com.spacece.milestonetracker.data.model

data class Chat(
    val id: String = "",
    val senderId: String = "",
    val senderImage: String = "",
    val senderName: String = "Unknown",
    val message: String = "",
    val timestamp: Long = 0,
)
