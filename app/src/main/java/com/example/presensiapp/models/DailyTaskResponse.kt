package com.example.presensiapp.models

data class DailyTaskResponse(
    val status: String,
    val message: String,
    val data: List<Task>
)

data class Task(
    val id: Int,
    val description: String,
    val location: String,
    val time: String
)
