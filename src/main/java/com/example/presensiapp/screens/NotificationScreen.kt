@file:JvmName("DailyTaskScreenKt")

package com.example.presensiapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.presensiapp.R
import com.example.presensiapp.widgets.CustomContainer

@Composable
fun NotificationScreen(navController: NavController) {
    var isNotificationEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0EADD)),
    ) {

        Column {
            CustomContainer(
                title = "Tugas Hari ini",
             onClick = {
                 navController.navigate("tugas")

             },
                drawableIcon = R.drawable.baseline_notifications_24,
                isIconEnabled = true

            )
            Spacer(Modifier.height(8.dp))
            CustomContainer(
                title = "Absen",
                onClick = {
                    navController.navigate("absen")

                },
                drawableIcon = R.drawable.baseline_notifications_24,
                isIconEnabled = true

            )
            Spacer(Modifier.height(8.dp))

            CustomContainer(
                title = "Riwayat Izin",
                onClick = {
                    navController.navigate("izinrecap")

                },
                drawableIcon = R.drawable.baseline_notifications_24,
                isIconEnabled = true
            )

            Spacer(Modifier.height(8.dp))

//            CustomContainer(
//                title = "Cek Status Izin",
//                onClick = {
//
//                },
//                drawableIcon = R.drawable.baseline_notifications_24,
//                isIconEnabled = true
//            )

        }

    }
}

