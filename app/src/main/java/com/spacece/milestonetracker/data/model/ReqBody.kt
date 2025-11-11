package com.spacece.milestonetracker.data.model

import java.io.File

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
)

data class AdminRegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val image: File? = null,
    val c_office: String? = null,
    val c_from_time: String? = null,
    val c_to_time: String? = null,
    val c_language: String? = null,
    val c_fee: String? = null,
    val selectedItem: String? = null,
    val c_qualification: String? = null,
    val c_available_days: String? = null
)

data class ForgotPasswordRequest(
    val email: String,
    val u_mobile: String,
    val password: String
)

data class UserVerifyRequest(
    val email: String,
    val otp: String,
)
