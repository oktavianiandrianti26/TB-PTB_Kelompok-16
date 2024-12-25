package com.example.presensiapp.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomRoundedButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Blue, // Warna latar default
    contentColor: Color = Color.White,  // Warna teks default
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), // Padding sekitar button
        shape = RoundedCornerShape(30.dp), // Sudut membulat
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, // Warna latar belakang
            contentColor = contentColor       // Warna teks
        )
    ) {
        Text(text = text)
    }
}
