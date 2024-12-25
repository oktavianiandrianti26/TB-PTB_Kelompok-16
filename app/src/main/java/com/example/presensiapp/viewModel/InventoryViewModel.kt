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
import com.example.presensiapp.models.InventoryData
import com.example.presensiapp.models.Permit
import com.example.presensiapp.models.PermitData
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

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PresensiRepository(RetrofitClient.apiService)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _inventoryResponse = MutableStateFlow<InventoryData?>(null)
    val inventoryResponse: StateFlow<InventoryData?> get() = _inventoryResponse
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage



    private val dataStoreManager = DataStoreManager(application)
    val userEmail = dataStoreManager.userEmail
    val userDataLocalStorage = dataStoreManager.getUserData
   val  userId = dataStoreManager.userId

fun submitInventory(
    name: RequestBody,
    datetime: RequestBody,
    description: RequestBody,
    type: RequestBody,
    userId: RequestBody,
    image1: MultipartBody.Part,
    image2: MultipartBody.Part,
    onSuccess: () -> Unit
) {
        Log.d("SUSKES", "SUKSES3")

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val userId = dataStoreManager.userId.firstOrNull()

                val userIdRequestBody = RequestBody.create(MultipartBody.FORM, userId.toString())

                val response = apiService.createInventory(name, datetime, description, type, userIdRequestBody, image1,image2)
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




    fun clearError() {
        _errorMessage.value = null
    }

}
