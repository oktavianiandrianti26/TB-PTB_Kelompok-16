package com.example.presensiapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.presensiapp.models.ApiResponse
import com.example.presensiapp.models.DailyTaskFormResponse
import com.example.presensiapp.models.DailyTaskResponse
import com.example.presensiapp.models.LoginRequest
import com.example.presensiapp.models.LoginResponse
import com.example.presensiapp.models.PermitResponse
import com.example.presensiapp.models.RegisterRequest
import com.example.presensiapp.models.RegisterResponse
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserResponse
import com.example.presensiapp.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class TaskRepository(private val apiService: ApiService) {

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData


    suspend fun fetchTask(): DailyTaskResponse {
        return apiService.fetchTask()
    }


    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("DATA", email+password)
        return apiService.login(LoginRequest(email, password))
    }
    suspend fun register(noHp: String, name: String, email: String, password: String): RegisterResponse {
        Log.d("REGISTER", name+email+password)

        return apiService.registerUser(RegisterRequest(name, email,noHp,password))
    }

    suspend fun submitDailyTask(userId: RequestBody, taskId: RequestBody, imagePart1: MultipartBody.Part, imagePart2: MultipartBody.Part, ): Response<DailyTaskFormResponse> {
        return try {
            apiService.submitTask(userId,taskId, imagePart1, imagePart2 )
        } catch (e: Exception) {
            throw Exception("An error occurred while submitting the task: ${e.message}")
        }
    }




}