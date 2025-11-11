package com.spacece.milestonetracker.data.repository

import android.content.Context
import com.spacece.milestonetracker.data.local.AppModule.aapDataBase
import com.spacece.milestonetracker.data.local.SharedPrefs
import com.spacece.milestonetracker.data.local.getCurrentUser
import com.spacece.milestonetracker.data.model.User

class UserRepository(context: Context) {
    private val appDatabase = aapDataBase(context)
    private val sharedPrefs = SharedPrefs(context)

    suspend fun getUserDetails(): User {
        return appDatabase.getCurrentUser()
    }
}
