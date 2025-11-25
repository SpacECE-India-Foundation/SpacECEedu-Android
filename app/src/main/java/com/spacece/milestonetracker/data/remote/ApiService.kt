package com.spacece.milestonetracker.data.remote

import com.spacece.milestonetracker.data.model.AnswersListReq
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.model.ChildData
import com.spacece.milestonetracker.data.model.ChildDetailsRes
import com.spacece.milestonetracker.data.model.ChildKaDetails
import com.spacece.milestonetracker.data.model.ForgetPasswordResponse
import com.spacece.milestonetracker.data.model.MilestoneTaskResponse
import com.spacece.milestonetracker.data.model.UpdateTaskStatusRequest
import com.spacece.milestonetracker.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun login(
        @Field(EMAIL) email: String,
        @Field(PASSWORD) password: String,
        @Field(TYPE) type: String,
        @Field(IS_API) isAPI: Boolean,
    ): Response<ApiResponse<User>>

    @Multipart
    @POST(SIGNUP)
    suspend fun signup(
        @Part(NAME) name: RequestBody,
        @Part(PHONE) phone: RequestBody,
        @Part(EMAIL) email: RequestBody,
        @Part(PASSWORD) password: RequestBody,
        @Part(TYPE) type: RequestBody,
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST(UPDATE_PASSWORD)
    suspend fun updatePassword(
        @Field(EMAIL) email: String,
        @Field(U_MOB) mobile: String,
        @Field(PASSWORD) newPassword: String,
    ): Response<ForgetPasswordResponse>

    @Multipart
    @POST(ADD_NEW_CHILD)
    suspend fun submitChildDetails(
        @Part image: MultipartBody.Part?,
        @Part(USER_ID) userId: RequestBody,
        @Part(CENTER) center: RequestBody,
        @Part(CHILD_NAME) childName: RequestBody,
        @Part(DOB) dob: RequestBody,
        @Part(GENDER) gender: RequestBody
    ): Response<ApiResponse<ChildDetailsRes>>


    @POST(UPDATE_CHILD_PROGRESS)
    suspend fun updateChildProgress(
        @Body requestBody: AnswersListReq
    ): Response<ApiResponse<Unit>>


    // api's of home fragment
    @FormUrlEncoded
    @POST(UPDATE_CHILD_GROWTH)
    suspend fun updateChildGrowth(
        @Query("userId") userId: Int,
        @Query("childId") childId: Int,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Response<ApiResponse<Unit>>


    @GET(GET_ALL_CHILD)
    suspend fun getAllChildren(
        @Query("userId") userId: Int
    ): Response<ApiResponse<ChildData>>

    @GET(GET_CHILD_DETAILS)
    suspend fun getChildDetails(
        @Query("userId") userId: Int,
        @Query("childId") childId: Int
    ): Response<ApiResponse<ChildKaDetails>>


    // api 6 7 8
    @GET(MILESTONE_TASK_LIST)
    suspend fun getMilestoneTasks(
        @Query("userId") userId: String,
        @Query("childId") childId: String
    ): Response<ApiResponse<MilestoneTaskResponse>>

    @POST(UPDATE_TASK_STATUS)
    suspend fun updateTaskStatus(
        @Query("userId") userId: String,
        @Query("childId") childId: String,
        @Body request: UpdateTaskStatusRequest
    ): Response<ApiResponse<Any>>

    @Multipart
    @POST(SUBMIT_MILESTONE_TASK)
    suspend fun submitMilestoneTask(
        @Part("userId") userId: RequestBody,
        @Part("childId") childId: RequestBody,
        @Part("taskId") taskId: RequestBody,
        @Part taskVideo: MultipartBody.Part
    ): Response<ApiResponse<Any>>

}
