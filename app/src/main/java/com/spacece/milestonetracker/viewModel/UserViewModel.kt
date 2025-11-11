package com.spacece.milestonetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.spacece.milestonetracker.data.model.User
import com.spacece.milestonetracker.data.repository.UserRepository

class UserViewModel(context: Context) : ViewModel() {
    private val userRepository = UserRepository(context)

    suspend fun getUserDetails(): User {
        return userRepository.getUserDetails()
    }
}