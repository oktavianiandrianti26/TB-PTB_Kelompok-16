package com.example.presensiapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.presensiapp.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF739072))
    ) {
        Image(
            painter = painterResource(id = R.drawable.spotless_logo), // Replace with your actual logo asset
            contentDescription = "App Logo",
            modifier = Modifier.align(Alignment.Center).width(333.dp).height(333.dp)
        )
    }
    // Delay for 2 seconds (adjust as needed)
    LaunchedEffect (Unit) {
        delay(2000)
        onSplashFinished()
    }
}
