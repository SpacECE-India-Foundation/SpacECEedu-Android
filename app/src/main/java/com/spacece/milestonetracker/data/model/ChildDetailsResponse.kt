package com.spacece.milestonetracker.data.model


data class ChildKaDetails(
    val childId: Int,
    val childName: String,
    val dob: String,
    val gender: String,
    val center: String,
    val image: String? = "https://static.vecteezy.com/system/resources/previews/007/312/854/large_2x/child-profile-sketch-vector.jpg",
    val height: Int,
    val weight: Int,
    val heightProgress: List<HeightProgress>,
    val weightProgress: List<WeightProgress>,
    val childProgress: List<ChildProgress>
)

data class HeightProgress(
    val date: String,
    val height: Int
)

data class WeightProgress(
    val date: String,
    val weight: Int
)

data class ChildProgress(
    val catId: String,
    val catName: String,
    val ans: ChildAnswers
)

data class ChildAnswers(
    val q1: String,
    val q2: String,
    val q3: String
)

