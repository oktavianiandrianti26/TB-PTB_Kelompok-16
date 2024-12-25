package com.example.presensiapp.models

data class RegisterResponse(
    val status: String,
    val message: String,
    val data: User
)

data class RegisterData(
    val id: Int,
    val email: String,
    val hp: String,
    val password: String,
    val created_at: String,
    val name: String?,
    val address: String?,
    val image: String?
)
