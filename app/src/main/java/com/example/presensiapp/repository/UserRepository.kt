package com.example.presensiapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.presensiapp.models.ApiResponse
import com.example.presensiapp.models.LoginRequest
import com.example.presensiapp.models.LoginResponse
import com.example.presensiapp.models.RegisterRequest
import com.example.presensiapp.models.RegisterResponse
import com.example.presensiapp.models.UpdateRequest
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserResponse
import com.example.presensiapp.service.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class UserRepository(private val apiService: ApiService) {

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData

    suspend fun fetchUser(userId: String) {
        withContext(Dispatchers.IO) {
            try {
//                val user = apiService.getUserById(userId)
                val user = apiService.getUserById()

//                _userLiveData.postValue(user)
                Log.d("TAG", user.toString())

            } catch (e: Exception) {
                e.printStackTrace() // Handle errors
            }
        }
    }


    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("DATA", email+password)
        return apiService.login(LoginRequest(email, password))
    }
    suspend fun register(noHp: String, name: String, email: String, password: String): RegisterResponse {
        Log.d("REGISTER", name+email+password)

        return apiService.registerUser(RegisterRequest(name, email,noHp,password))
    }

    suspend fun updateUser(user: User): Response<Unit> {
//        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), user.name)
//        val hpPart = RequestBody.create("text/plain".toMediaTypeOrNull(), user.hp)
//        val addressPart = RequestBody.create("text/plain".toMediaTypeOrNull(), "Dfsdfsdf")
//        val image = RequestBody.create("text/plain".toMediaTypeOrNull(), user.image)

        // Jika `image` adalah path ke file
        val imagePart = if (user.image.isNotEmpty()) {
            val file = File(user.image)
            val imageRequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, imageRequestBody)
        } else {
            null
        }

        return apiService.updateUser(
//            name = user.name,
//            nohp = user.hp!!,
//            address = user.address,
////            image = image
            UpdateRequest(user.name,user.email,user.hp,user.password, user.address)
        )
    }

}