package com.spacece.milestonetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.spacece.milestonetracker.data.local.Converters
import com.spacece.milestonetracker.data.local.ROOM_ENTITY_USER

@Entity(tableName = ROOM_ENTITY_USER)
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey val current_user_id: Int = 0,
    val current_user_name: String = "",
    val current_user_email: String = "",
    val current_user_mob: String = "",
    val current_user_type: String = "",
    val current_user_image: String = ""
)
