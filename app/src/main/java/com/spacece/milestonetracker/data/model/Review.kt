package com.spacece.milestonetracker.data.model

import com.spacece.milestonetracker.utils.UIState

data class Review(
    val name: String? = "",
    val image: String? = "",
    val stars: Double? = 0.0,
    val review: String? = "",
    val timestamp: Long? = 0,
    var isLastItem: Boolean = false,
    var uiState: UIState = UIState.DataView
)
