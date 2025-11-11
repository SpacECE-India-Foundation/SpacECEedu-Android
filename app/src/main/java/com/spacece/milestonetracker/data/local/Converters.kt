package com.spacece.milestonetracker.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.spacece.milestonetracker.data.model.User

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(userData: User?): String? {
        if (userData == null) return null
        return gson.toJson(userData)
    }

    @TypeConverter
    fun toUser(userString: String?): User? {
        if (userString == null) return null
        return gson.fromJson(userString, User::class.java)
    }
}