package com.example.presensiapp.models

data class DailyTaskFormResponse(
    val status: String,
    val message: String,
    val data: TaskData
)

data class TaskData(
    val id: Int,
    val user_id: Int,
    val image1: String,
    val image2: String,
    val datetime: String
)
