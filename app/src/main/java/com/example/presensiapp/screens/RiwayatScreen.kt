package com.example.presensiapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.presensiapp.R
import com.example.presensiapp.widgets.CustomContainer

@Composable
fun RiwayatScreen(navController: NavController) {
    var isNotificationEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0EADD)),
    ) {

        Column {
            Row(modifier = Modifier.align(Alignment.Start)){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(bottom = 10.dp)
                )
                Text(
                    text = "Riwayat",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomContainer(
                title = "Riwayat Absensi",
             onClick = {
                 navController.navigate("recapitulation")

             },
                drawableIcon = R.drawable.ic_launcher_foreground,
                isIconEnabled = true

            )
            Spacer(Modifier.height(8.dp))
            CustomContainer(
                title = "Riwayat Izin",
                onClick = {
                    navController.navigate("izin")

                },
                drawableIcon = R.drawable.ic_launcher_foreground,
                isIconEnabled = true

            )
            Spacer(Modifier.height(8.dp))

            CustomContainer(
                title = "Laporan Kinerja",
                onClick = {
                    navController.navigate("performance")

                },
                drawableIcon = R.drawable.ic_launcher_foreground,
                isIconEnabled = true
            )
        }

    }
}

