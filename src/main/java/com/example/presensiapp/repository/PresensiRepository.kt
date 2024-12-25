package com.example.presensiapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.presensiapp.models.ApiResponse
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

class PresensiRepository(private val apiService: ApiService) {

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData

//    suspend fun fetchPresensi(userId: String) {
//        withContext(Dispatchers.IO) {
//            try {
////                val user = apiService.getUserById(userId)
//                val presensi = apiService.fetchPresensi(userId)
//
////                _userLiveData.postValue(user)
//                Log.d("TAG", presensi.toString())
//
//            } catch (e: Exception) {
//                e.printStackTrace() // Handle errors
//            }
//        }
//    }

    suspend fun fetchPresensi(userId:String): PermitResponse {
        Log.d("USER ID", userId)
        return apiService.fetchPresensi(userId)
    }


    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("DATA", email+password)
        return apiService.login(LoginRequest(email, password))
    }
    suspend fun register(noHp: String, name: String, email: String, password: String): RegisterResponse {
        Log.d("REGISTER", name+email+password)

        return apiService.registerUser(RegisterRequest(name, email,noHp,password))
    }

}