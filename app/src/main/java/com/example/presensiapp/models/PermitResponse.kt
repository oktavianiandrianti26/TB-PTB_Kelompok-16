package com.example.presensiapp.models

data class PermitResponse(
    val status: String,
    val message: String,
    val data: PermitData
)

data class PermitData(
    val countPermit: Int,
    val permitData: List<Permit>
)

data class Permit(
    val id: Int? = null,
    val name: String? = null,
    val datetime: String? = null,
    val location: String? = null,
    val description: String? = null,
    val image: String? = null,
    val createdAt: String? = null,
    val type: String? = null,
    val userId: Int? = null,
    val day: String? = null
)