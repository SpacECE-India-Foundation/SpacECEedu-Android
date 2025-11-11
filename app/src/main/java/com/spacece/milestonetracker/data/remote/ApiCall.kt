package com.spacece.milestonetracker.data.remote

import com.spacece.milestonetracker.data.model.ApiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

object ApiCall {
    inline fun <T> safeApiCall(apiCall: () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: UNKNOWN_ERROR))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun <T> isTaskSuccess(response: Response<ApiResponse<T>>): Boolean {
        return if (response.isSuccessful && response.code() == STATUS_CODE_SUCCESS) {
            when (response.body()?.status) {
                STATUS_SUCCESS -> true
                else -> false
            }
        } else false
    }

    fun String.toRequestBody(): RequestBody {
        return this.toRequestBody(TYPE_TEXT.toMediaTypeOrNull())
    }
}
