package com.spacece.milestonetracker.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacece.milestonetracker.viewModel.vmHelper.Event
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.model.ForgetPasswordResponse
import com.spacece.milestonetracker.data.model.ForgotPasswordRequest
import com.spacece.milestonetracker.data.model.LoginRequest
import com.spacece.milestonetracker.data.model.SignupRequest
import com.spacece.milestonetracker.data.model.User
import com.spacece.milestonetracker.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    private val authRepository = AuthRepository(context)

    private val _loginResponse = MutableLiveData<Event<Result<ApiResponse<User>>>>()
    val loginResponse: LiveData<Event<Result<ApiResponse<User>>>> get() = _loginResponse

    private val _signupResponse = MutableLiveData<Event<Result<ApiResponse<Any>>>>()
    val parentRegisterResponse: LiveData<Event<Result<ApiResponse<Any>>>> get() = _signupResponse

    private val _forgetResponse = MutableLiveData<Event<Result<ForgetPasswordResponse>>>()
    val forgetResponse: LiveData<Event<Result<ForgetPasswordResponse>>> get() = _forgetResponse

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResponse.value = Event(authRepository.login(request))
        }
    }

    fun signup(request: SignupRequest) {
        viewModelScope.launch {
            _signupResponse.value = Event(authRepository.signup(request))
        }
    }

    fun forgetPassword(request: ForgotPasswordRequest) {
        viewModelScope.launch {
            _forgetResponse.value = Event(authRepository.forgetPassword(request))
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}