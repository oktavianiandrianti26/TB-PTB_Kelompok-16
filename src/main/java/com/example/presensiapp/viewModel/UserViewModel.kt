package com.example.presensiapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presensiapp.datastore.DataStoreManager
import com.example.presensiapp.models.LoginResponse
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserResponse
import com.example.presensiapp.repository.UserRepository
import com.example.presensiapp.service.ApiService
import com.example.presensiapp.service.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(RetrofitClient.apiService)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val userLiveData: LiveData<User> = repository.userLiveData
    private val _loginResponse = MutableStateFlow<User?>(null)
    val loginResponse: StateFlow<User?> get() = _loginResponse
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage



    private val dataStoreManager = DataStoreManager(application)
    val userEmail = dataStoreManager.userEmail
    val userDataLocalStorage = dataStoreManager.getUserData

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(email, password)
                Log.d("RESPONSE LOGIN", response.toString());
                _loginResponse.value = response.data.user

                // Simpan ke DataStore
                dataStoreManager.saveUser(response.data.user)

                Log.d("RESPONSE", response.data.user.email.toString())
                if(response.status == "success"){
                    onLoginSuccess()
                }else{
                    throw Exception(response.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ERROR MESSAGE",e.toString())
//                _errorMessage.value = e.message ?: "An unknown error occurred"
                val errorMessage = try {
                    val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.getString("message") // Ambil pesan error dari JSON
                } catch (parseException: Exception) {
                    "An unknown error occurred"
                }

                _errorMessage.value = errorMessage

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, noHp: String, password: String, onRegisterSucces: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(noHp,name, email, password)
                Log.d("RESPONSE REGISTER", response.toString())

                // Ambil data hasil registrasi
                val userData = response.data
                Log.d("REGISTER SUCCESS", "User ${userData.email} berhasil didaftarkan")

                // Simpan email di DataStore jika diperlukan
                dataStoreManager.saveUser(userData)
                _loginResponse.value = response.data
                if(response.status == "success"){
                    onRegisterSucces()
                }else{
                    throw Exception(response.message)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ERROR REGISTER", e.toString())

                val errorMessage = try {
                    val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.getString("message")
                } catch (parseException: Exception) {
                    "An unknown error occurred during registration"
                }

                _errorMessage.value = errorMessage

            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updateUser(user)

                // Ambil data hasil registrasi
                val userData = response

                // Simpan email di DataStore jika diperlukan
//                dataStoreManager.saveUser(userData)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ERROR REGISTER", e.toString())

                val errorMessage = try {
                    val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.getString("message")
                } catch (parseException: Exception) {
                    "An unknown error occurred during registration"
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
