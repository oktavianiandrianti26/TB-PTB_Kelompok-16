package com.example.presensiapp.reusable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RoundedImageCard(
    drawableResId: Int,
    contentDescription: String,
    text: String,
    onClick: () -> Unit

) {
    Card(

        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
        .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),

        colors = CardColors(containerColor = Color(0xFF8B9C8B), contentColor = Color(0xFFFFFFFF  ), disabledContentColor = Color(0xFF0000FF), disabledContainerColor = Color(0xFFFFA500))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan gambar
                Image(
                    painter = painterResource(id = drawableResId),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF8B9C8B)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                fontWeight = FontWeight.Bold,
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        Spacer(modifier = Modifier.height(8.dp))

    }

}


@Composable
fun DashboardCard(
    text: String,
    count: String,
    onClick: () -> Unit

) {
    Card(

        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),

        colors = CardColors(containerColor = Color(0xFF8B9C8B), contentColor = Color(0xFFFFFFFF  ), disabledContentColor = Color(0xFF0000FF), disabledContainerColor = Color(0xFFFFA500))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan gambar
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                fontWeight = FontWeight.Normal,
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                fontWeight = FontWeight.Bold,
                text = count,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
