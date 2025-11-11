package com.spacece.milestonetracker.data.model

data class ApiResponse<T>(
    val status: String? = "",
    val message: String? = "",
    val data: T? = null
)

data class ForgetPasswordResponse(
    val message: String? = null,
    val error: String? = null
)