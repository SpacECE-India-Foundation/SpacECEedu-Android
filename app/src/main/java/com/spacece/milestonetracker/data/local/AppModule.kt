package com.spacece.milestonetracker.data.local

import android.content.Context
import androidx.room.Room

object AppModule {
    fun aapDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, ROOM_DB).build()
    }
}