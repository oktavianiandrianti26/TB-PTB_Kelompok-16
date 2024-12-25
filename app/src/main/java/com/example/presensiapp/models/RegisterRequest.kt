package com.example.presensiapp.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val hp: String,
    val password: String
)

data class UpdateRequest(
    val name: String,
    val email: String,
    val hp: String,
    val password: String,
    val address: String
)


data class IzinRequest(
    val name :String,
    val datetime : String,
    val description : String,
    val type : String,
    val userId : Int // Contoh userId
)

data class TaskReport(
    val date: String,
    val description: List<String>
)