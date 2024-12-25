package com.example.presensiapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presensiapp.datastore.DataStoreManager
import com.example.presensiapp.models.AbsenRequest
import com.example.presensiapp.models.DailyTaskResponse
import com.example.presensiapp.models.Permit
import com.example.presensiapp.models.PermitData
import com.example.presensiapp.models.Task
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserResponse
import com.example.presensiapp.repository.PresensiRepository
import com.example.presensiapp.repository.TaskRepository
import com.example.presensiapp.repository.UserRepository
import com.example.presensiapp.service.ApiService
import com.example.presensiapp.service.RetrofitClient
import com.example.presensiapp.service.RetrofitClient.apiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(RetrofitClient.apiService)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _taskReponse = MutableStateFlow<List<Task?>>(emptyList())
    val taskResponse: StateFlow<List<Task?>> get() = _taskReponse
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage



    private val dataStoreManager = DataStoreManager(application)
    val userEmail = dataStoreManager.userEmail
    val userDataLocalStorage = dataStoreManager.getUserData
   val  userId = dataStoreManager.userId

    fun fetchTask() {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = dataStoreManager.userId.firstOrNull()
                val response = repository.fetchTask()
                Log.d("RESPONSE LOGIN", response.toString());
                _taskReponse.value = response.data

                Log.d("RESPONSE", response.data.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ERROR MESSAGE",e.toString())
                val errorMessage = try {
                    val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.getString("message")
                } catch (parseException: Exception) {
                    "An unknown error occurred"
                }

                _errorMessage.value = errorMessage

            } finally {
                _isLoading.value = false
            }
        }
    }


    fun submitDailyTask(imagePart1: MultipartBody.Part, imagePart2: MultipartBody.Part, taskId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = dataStoreManager.userId.firstOrNull()

                val userIdRequestBody = RequestBody.create(MultipartBody.FORM, userId.toString())
                val taskId = RequestBody.create(MultipartBody.FORM, taskId.toString())
                val response = repository.submitDailyTask(userIdRequestBody,taskId, imagePart1, imagePart2)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    throw Exception("You Have already submit task")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = try {
                    val errorBody = (e as? HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    Log.d("ERROR BODY", errorBody ?: "No error body")

                    jsonError.optString("error", jsonError.optString("message", "Unknown error")) // Prioritas pada "error", lalu "message"
                } catch (parseException: Exception) {
                    "${e.message.toString()}"
                }

                _errorMessage.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }

    // Fungsi untuk mendapatkan email yang disimpan

}
