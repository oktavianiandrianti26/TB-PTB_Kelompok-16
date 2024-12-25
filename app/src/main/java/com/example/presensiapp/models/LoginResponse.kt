package com.example.presensiapp.models

data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val user: User
)

data class User(
    val id: Int,
    val email: String,
    val hp: String,
    val password: String,
    val created_at: String,
    val name: String,
    val address: String,
    val image: String
)