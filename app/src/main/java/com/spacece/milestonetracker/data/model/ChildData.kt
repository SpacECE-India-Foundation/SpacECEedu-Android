package com.spacece.milestonetracker.data.model


data class ChildData(
    val children: List<Child>
)

data class Child(
    val childId: Int,
    val childName: String,
    val dob: String,
    val gender: String,
    val center: String,
    val image: String? = "https://static.vecteezy.com/system/resources/previews/007/312/854/large_2x/child-profile-sketch-vector.jpg",
    val height: Int?,
    val weight: Int?
)
