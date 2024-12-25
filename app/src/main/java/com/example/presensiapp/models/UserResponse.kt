package com.example.presensiapp.models

data class UserResponse(
//    val id: String,
//    val name: String,
//    val email: String
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

data class ApiResponse<T>(
    val body: T?,
    val error: String?
)