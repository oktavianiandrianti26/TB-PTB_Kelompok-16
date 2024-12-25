package com.example.presensiapp.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presensiapp.R

@Composable
fun CustomContainer(
    title: String,
    drawableIcon: Int,
    isIconEnabled: Boolean,
    onClick: ()->Unit
    ) {
    // Warna ikon berdasarkan status notifikasi
    val iconTint = if (isIconEnabled) Color(0xFF000000) else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(51.dp)
            .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }, // Klik untuk toggle
        verticalAlignment = Alignment.CenterVertically
    ) {
       if (isIconEnabled){
           Icon(
               painter = painterResource(id = drawableIcon),
               contentDescription = "Notification Icon",
               tint = iconTint,
               modifier = Modifier
                   .size(40.dp)
                   .padding(4.dp)

           )
       }
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
