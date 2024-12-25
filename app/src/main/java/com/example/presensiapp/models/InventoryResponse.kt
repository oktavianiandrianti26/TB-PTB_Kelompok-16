package com.example.presensiapp.models

data class InventoryResponse(
    val status: String,
    val message: String,
    val data: InventoryData
)

data class InventoryData(
    val id: Int,
    val name: String,
    val datetime: String,
    val description: String,
    val image1: String,
    val image2: String,
    val created_at: String
)