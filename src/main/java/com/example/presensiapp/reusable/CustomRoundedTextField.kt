package com.example.presensiapp.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    maxLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Label di atas TextField
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // TextField dengan bentuk rounded dan placeholder
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            maxLines = maxLines,
            shape = RoundedCornerShape(24.dp), // Membuat bentuk rounded
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFFFFFFF), // Warna latar belakang TextField
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}