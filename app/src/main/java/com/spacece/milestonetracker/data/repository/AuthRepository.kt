package com.spacece.milestonetracker.data.repository

import android.content.Context
import com.spacece.milestonetracker.data.local.AppModule.aapDataBase
import com.spacece.milestonetracker.data.local.SharedPrefs
import com.spacece.milestonetracker.data.local.clearAllData
import com.spacece.milestonetracker.data.local.initialiseUser
import com.spacece.milestonetracker.data.model.*
import com.spacece.milestonetracker.data.remote.ApiCall.isTaskSuccess
import com.spacece.milestonetracker.data.remote.ApiCall.safeApiCall
import com.spacece.milestonetracker.data.remote.ApiCall.toRequestBody
import com.spacece.milestonetracker.data.remote.ApiModule.apiService

class AuthRepository(context: Context) {
    private val appDatabase = aapDataBase(context)
    private val sharedPrefs = SharedPrefs(context)

    suspend fun login(request: LoginRequest): Result<ApiResponse<User>> {
        return safeApiCall {
            val response = apiService().login(
                request.email,
                request.password,
                sharedPrefs.getUserType(),
                true
            )
            if (isTaskSuccess(response)) {
                appDatabase.initialiseUser(sharedPrefs, response.body()?.data)
            }
            response
        }
    }

    suspend fun signup(request: SignupRequest): Result<ApiResponse<Any>> {
        return safeApiCall {
            apiService().signup(
                request.name.toRequestBody(),
                request.phone.toRequestBody(),
                request.email.toRequestBody(),
                request.password.toRequestBody(),
                sharedPrefs.getUserType().toRequestBody()
            )
        }
    }

    suspend fun forgetPassword(request: ForgotPasswordRequest): Result<ForgetPasswordResponse> {
        return safeApiCall {
            val response =
                apiService().updatePassword(request.email, request.u_mobile, request.password)
            response
        }
    }

    suspend fun logout() {
        appDatabase.clearAllData(sharedPrefs)
    }
}
