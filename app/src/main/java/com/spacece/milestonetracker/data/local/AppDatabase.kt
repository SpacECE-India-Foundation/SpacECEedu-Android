package com.spacece.milestonetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spacece.milestonetracker.data.local.dao.UserDao
import com.spacece.milestonetracker.data.model.User

@Database(
    entities = [User::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}