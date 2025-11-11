package com.spacece.milestonetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spacece.milestonetracker.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): User?

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}