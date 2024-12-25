package com.example.presensiapp.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryRequest(
    val name: String,
    val datetime: String,
    val description: String,
    val image1: String,
    val image2: String,
    val type: String,
    var userId: String
): Parcelable