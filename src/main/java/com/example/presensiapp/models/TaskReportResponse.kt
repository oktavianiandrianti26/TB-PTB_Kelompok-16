package com.example.presensiapp.models

data class TaskReportResponse(
    val status: String,
    val message: String,
    val data: ReportData
)

data class ReportData(
    val countReport: Int,
    val reportData: List<ReportItem>
)

data class ReportItem(
    val description: String
)