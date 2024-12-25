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
import com.example.presensiapp.models.IzinRequest
import com.example.presensiapp.models.Permit
import com.example.presensiapp.models.PermitData
import com.example.presensiapp.models.ReportData
import com.example.presensiapp.models.TaskReport
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserResponse
import com.example.presensiapp.repository.PresensiRepository
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

class PresensiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PresensiRepository(RetrofitClient.apiService)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _presensiResponse = MutableStateFlow<PermitData?>(null)
    val presensiResponse: StateFlow<PermitData?> get() = _presensiResponse
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage



    private val dataStoreManager = DataStoreManager(application)
    val userEmail = dataStoreManager.userEmail
    val userDataLocalStorage = dataStoreManager.getUserData
   val  userId = dataStoreManager.userId

fun submitAbsen(
    name: RequestBody,
    datetime: RequestBody,
    location: RequestBody,
    description: RequestBody,
    type: RequestBody,
    userId: RequestBody,
    image: MultipartBody.Part,
    onSuccess: () -> Unit
) {
        Log.d("SUSKES", "SUKSES3")

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val userId = dataStoreManager.userId.firstOrNull()

                val userIdRequestBody = RequestBody.create(MultipartBody.FORM, userId.toString())

                val response = apiService.submitAbsen(name, datetime, location, description, type, userIdRequestBody, image)
                onSuccess()
                if (response.isSuccessful) {
                    Log.d("SUSKES", "SUKSES")
                } else {
                }
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

    fun submitIzin(
        name: String,
        datetime: String,
        description: String,
        type: String,
        userId: String,
        onSuccess: () -> Unit
    ) {
        Log.d("SUSKES", "SUKSES3")

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val userId = dataStoreManager.userId.firstOrNull()?.toInt()

//                val userIdRequestBody = RequestBody.create(MultipartBody.FORM, userId.toString())

                val response = apiService.submitIzin(IzinRequest(
                    name,
                    datetime,
                    description,
                    type,
                    userId!!,
                ))
                if (response.isSuccessful) {
                    onSuccess()
                    Log.d("SUSKES", "SUKSES")
                } else {
                    throw Exception("You Have already submit izin")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ERROR MESSAGE",e.toString())
                val errorMessage = try {
                    val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.getString("message")
                } catch (parseException: Exception) {
                   e.message.toString()
                }

                _errorMessage.value = errorMessage

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPresensi() {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = dataStoreManager.userId.firstOrNull()
                val response = repository.fetchPresensi(userId.toString())
                Log.d("RESPONSE LOGIN", response.toString());
                _presensiResponse.value = response.data

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


    private val _taskReport = MutableStateFlow<ReportData?>(null)
    val taskReport: StateFlow<ReportData?> get() = _taskReport

    fun fetchTaskReport() {

        viewModelScope.launch {
            val userId = dataStoreManager.userId.firstOrNull()


            try {
                val response = apiService.getTaskReport(userId!!.toInt())
                _taskReport.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun clearError() {
        _errorMessage.value = null
    }

    // Fungsi untuk mendapatkan email yang disimpan

}
