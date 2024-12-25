package com.example.presensiapp.helper

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CurrentDateDisplay() {
    // Get the current date
    val currentDate = Calendar.getInstance().time

    // Define the desired format
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")) // Indonesian Locale for "Maret"

    // Format the date
    val formattedDate = formatter.format(currentDate)

    // Display the date in a Text composable
    Text(text = formattedDate,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
       )
}