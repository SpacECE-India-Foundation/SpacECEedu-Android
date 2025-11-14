package com.spacece.milestonetracker.data.model

import okhttp3.MultipartBody

data class CategoryList(
    val categories: List<Category>
)

data class Category(
    val catId: Int,
    val catName: String,
    val que: Map<String, String>
)

data class ChildDetails(
    val name: String,
    val dob: Long,
    val gender: String,
    val center: String,
)

// Add Child Request Model
data class ChildDetailsReq(
    val userId:Int,
    val childImage:MultipartBody.Part?,
    val childName: String,
    val dob: Long,
    val gender: String,
    val center: String,
)

data class AnswersList(
    val childId: Int,
    val questionId: Int,
    val answers: List<AnswerReq>
)
// Update Child Progress Request Model
data class AnswersListReq(
    val userId: Int,
    val childId: Int,
    val questionsId: Int,
    val answers: List<AnswerReq>
)

data class AnswerReq(
    val catId: Int,
    val catName: String,
    val ans: Map<String,String>
)

data class ChildDetailsRes(
    val childId: Int,
    val childName: String,
    val dob: String,
    val gender: String,
    val center: String,
    val image: String?,
    val questionsId: String,
    val questions: List<Category>
)