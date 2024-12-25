package com.example.presensiapp.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AbsenRequest(
    val name: String,
    val datetime: String,
    val location: String,
    val description: String,
    val image: String,
    val type: String,
    var userId: String
): Parcelable