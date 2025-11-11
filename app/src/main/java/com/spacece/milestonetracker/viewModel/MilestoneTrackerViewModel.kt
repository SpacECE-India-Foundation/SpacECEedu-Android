package com.spacece.milestonetracker.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacece.milestonetracker.data.local.AppModule.aapDataBase
import com.spacece.milestonetracker.data.model.AnswersList
import com.spacece.milestonetracker.data.model.AnswersListReq
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.model.ChildDetails
import com.spacece.milestonetracker.data.model.ChildDetailsReq
import com.spacece.milestonetracker.data.model.ChildDetailsRes
import com.spacece.milestonetracker.data.model.ChildData
import com.spacece.milestonetracker.data.model.ChildKaDetails
import com.spacece.milestonetracker.data.repository.MilestoneTrackerRepository
import com.spacece.milestonetracker.viewModel.vmHelper.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody




class MilestoneTrackerViewModel(context: Context) : ViewModel() {
    private val milestoneTrackerRepository = MilestoneTrackerRepository(context)
    private val appDatabase = aapDataBase(context)
    private var userId: Int? = null



    private val _childId = MutableLiveData<Event<String>>()

    private val _addChildRequest = MutableLiveData<Event<Result<ApiResponse<ChildDetailsReq>>>>()
    val addChildRequest: LiveData<Event<Result<ApiResponse<ChildDetailsReq>>>> get() = _addChildRequest

    private val _addChildResponse = MutableLiveData<Event<Result<ApiResponse<ChildDetailsRes>>>>()
    val addChildResponse: LiveData<Event<Result<ApiResponse<ChildDetailsRes>>>> get() = _addChildResponse

    private val _updateProgressResponse = MutableLiveData<Event<Result<ApiResponse<Unit>>>>()
    val updateProgressResponse: LiveData<Event<Result<ApiResponse<Unit>>>> get() = _updateProgressResponse

    fun updateChildId(newId: String) {
        _childId.value = Event(newId)
    }

    fun submitChildDetails(imagePart: MultipartBody.Part?, dataPart: ChildDetails) {
        viewModelScope.launch {
            val id = getUserId()
            if (id == null) {
                _addChildResponse.value = Event(Result.failure(Exception("User not logged in")))
                return@launch
            }
            userId = id
            _addChildResponse.value = Event(
                milestoneTrackerRepository.submitChildDetails(
                    ChildDetailsReq(
                        id,
                        imagePart,
                        dataPart.name,
                        dataPart.dob,
                        dataPart.gender,
                        dataPart.center
                    )
                )
            )
        }
    }

    fun updateChildProgress(
        answers: AnswersList
    ) {
        viewModelScope.launch {
            val id = userId ?: getUserId()
            if (id == null) {
                _updateProgressResponse.value = Event(Result.failure(Exception("User not logged in")))
                return@launch
            }
            userId = id
            _updateProgressResponse.value =
                Event(milestoneTrackerRepository.updateChildProgress(AnswersListReq(id,answers.childId,answers.questionId,answers.answers)))
        }
    }

    suspend fun getUserId(): Int? {
        return withContext(Dispatchers.IO) {
            val user = appDatabase.userDao().getUser()
            user?.current_user_id
        }
    }





//    for get all child api
    private val _childrenResponse = MutableLiveData<Event<Result<ApiResponse<ChildData>>>>()
    val childrenResponse: LiveData<Event<Result<ApiResponse<ChildData>>>> get() = _childrenResponse

    fun fetchAllChildren(userId: Int) {
        viewModelScope.launch {
            val result = milestoneTrackerRepository.getAllChildren(userId)
            _childrenResponse.value = Event(result)
        }
    }

    private val _updateChildGrowthResponse = MutableLiveData<Event<Result<ApiResponse<Unit>>>>()
    val updateChildGrowthResponse: LiveData<Event<Result<ApiResponse<Unit>>>> get() = _updateChildGrowthResponse

    fun updateChildGrowth(userId: Int, childId: Int, height: String, weight: String) {
        viewModelScope.launch {
            _updateChildGrowthResponse.value = Event(milestoneTrackerRepository.updateChildGrowth(userId, childId, height, weight))
        }
    }

    private val _childDetailsResponse =
        MutableLiveData<Event<Result<ApiResponse<ChildKaDetails>>>>()
    val childDetailsResponse: LiveData<Event<Result<ApiResponse<ChildKaDetails>>>>
        get() = _childDetailsResponse

    fun getChildDetails(userId: Int?, childId: Int) {
        viewModelScope.launch {
            try {
                val result = milestoneTrackerRepository.getChildDetails(userId!!, childId)
                _childDetailsResponse.postValue(Event(result))
            } catch (e: Exception) {
                _childDetailsResponse.postValue(
                    Event(Result.failure(e))
                )
            }
        }
    }
}