package com.spacece.milestonetracker.data.repository

import android.content.Context
import android.util.Log
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.model.AnswersListReq
import com.spacece.milestonetracker.data.model.ChildData
import com.spacece.milestonetracker.data.model.ChildDetailsReq
import com.spacece.milestonetracker.data.model.ChildDetailsRes
import com.spacece.milestonetracker.data.model.ChildKaDetails
import com.spacece.milestonetracker.data.remote.ApiCall.safeApiCall
import com.spacece.milestonetracker.data.remote.ApiCall.toRequestBody
import com.spacece.milestonetracker.data.remote.ApiModule.apiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MilestoneTrackerRepository(context: Context) {
    private val userRepository = UserRepository(context)

    /*suspend fun submitChildDetails(
        details: ChildDetailsReq
    ): Result<ApiResponse<ChildDetailsRes>> {
        Log.d("IMG", "Image Part: ${if (details.image != null) "Present" else "NULL"}")
        if (details.image != null) {
            Log.d("IMG", "Image Part Body: ${details.image.body}")
            Log.d("IMG", "Image Part Headers: ${details.image.headers}")
        }
        return safeApiCall {
            apiService().submitChildDetails(
                details.image,
                details.userId.toString().toRequestBody(),
                details.center.toRequestBody(),
                details.childName.toRequestBody(),
                convertDobToRequestBody(details.dob),
                details.gender.toRequestBody()
            )
        }
    }*/

    suspend fun submitChildDetails(
        details: ChildDetailsReq
    ): Result<ApiResponse<ChildDetailsRes>> {

        Log.d("SubmitChild", "═══════════════════════════════════")
        Log.d("SubmitChild", "📤 SUBMITTING CHILD DETAILS")
        Log.d("SubmitChild", "───────────────────────────────────")
        Log.d("SubmitChild", "UserId: ${details.userId}")
        Log.d("SubmitChild", "ChildName: ${details.childName}")
        Log.d("SubmitChild", "Center: ${details.center}")
        Log.d("SubmitChild", "Gender: ${details.gender}")
        Log.d("SubmitChild", "DOB: ${details.dob}")
        Log.d("SubmitChild", "───────────────────────────────────")

        if (details.childImage != null) {
            Log.d("SubmitChild", "✅ Image: PRESENT")
            Log.d("SubmitChild", "   Field Name: image")
            Log.d("SubmitChild", "   Headers: ${details.childImage.headers}")
            Log.d("SubmitChild", "   Content-Type: ${details.childImage.body.contentType()}")
            Log.d("SubmitChild", "   Content-Length: ${details.childImage.body.contentLength()} bytes")
            Log.d("SubmitChild", "   Size: ${details.childImage.body.contentLength() / 1024} KB")
        } else {
            Log.e("SubmitChild", "❌ Image: NULL - No image provided!")
        }

        Log.d("SubmitChild", "═══════════════════════════════════")

        val result = safeApiCall {
            apiService().submitChildDetails(
                details.childImage,
                details.userId.toString().toRequestBody(),
                details.center.toRequestBody(),
                details.childName.toRequestBody(),
                convertDobToRequestBody(details.dob),
                details.gender.toRequestBody()
            )
        }

        // ✅ LOG THE RESPONSE
        result.onSuccess { response ->
            Log.d("SubmitChild", "───────────────────────────────────")
            Log.d("SubmitChild", "📥 API RESPONSE SUCCESS")
            Log.d("SubmitChild", "Status: ${response.status}")
            Log.d("SubmitChild", "Message: ${response.message}")
            Log.d("SubmitChild", "Child ID: ${response.data?.childId}")
            Log.d("SubmitChild", "Child Name: ${response.data?.childName}")
            Log.d("SubmitChild", "───────────────────────────────────")

            // 🔴 THIS IS THE KEY - Check if backend returns image URL
            if (response.data?.image == null) {
                Log.e("SubmitChild", "⚠️⚠️⚠️ BACKEND ISSUE CONFIRMED ⚠️⚠️⚠️")
                Log.e("SubmitChild", "Image was sent but backend returned NULL!")
                Log.e("SubmitChild", "This is NOT an Android issue.")
                Log.e("SubmitChild", "Share these logs with backend team.")
            } else {
                Log.d("SubmitChild", "✅ Image URL: ${response.data.image}")
            }

            Log.d("SubmitChild", "═══════════════════════════════════")
        }

        result.onFailure { error ->
            Log.e("SubmitChild", "❌ API Response Failed")
            Log.e("SubmitChild", "Error: ${error.message}", error)
        }

        return result
    }


    suspend fun updateChildProgress(
        body: AnswersListReq
    ): Result<ApiResponse<Unit>> {
        return safeApiCall {
            val response = apiService().updateChildProgress(body)
            response
        }
    }

    private fun convertDobToRequestBody(dobMillis: Long): RequestBody {
        val date = Date(dobMillis)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dobString = formatter.format(date)
        return dobString.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    suspend fun getAllChildren(userId: Int): Result<ApiResponse<ChildData>> {
        return safeApiCall {
            val response = apiService().getAllChildren(userRepository.getUserDetails().current_user_id)
            response
        }
    }

    suspend fun updateChildGrowth(
        userId: Int,
        childId: Int,
        height: String,
        weight: String
    ): Result<ApiResponse<Unit>> {
        return try {
            val response = apiService().updateChildGrowth(userId, childId, height, weight)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getChildDetails(userId: Int, childId: Int): Result<ApiResponse<ChildKaDetails>>  {
        return safeApiCall {
            apiService().getChildDetails(userId, childId)
        }
    }

}