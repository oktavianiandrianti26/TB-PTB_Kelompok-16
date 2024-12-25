package com.example.presensiapp.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.presensiapp.R
import com.example.presensiapp.reusable.RoundedImageCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(imageItems: List<Pair<Int, String>>, navController: NavHostController) {

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol back custom
//            IconButton(onClick = {
//                Log.d("Click", "Clicked")
//            }) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_ios_new_24), // Ganti dengan ikon back Anda
//                    contentDescription = "Back",
//                    tint = Color.Black
//                )
//            }

            Spacer(modifier = Modifier.width(8.dp))

            // Title
            Text(
                text = "Home",
                fontSize = 20.sp,
                color = Color.Black
            )
        }


        Box(
            modifier = Modifier.padding(8.dp)

        ) {
            RoundedImageCard(
                drawableResId = R.drawable.absen,
                contentDescription = "Image1",
                text = "Absen",
               onClick = {
                   Log.d("Clickedd","CLICKED")
                   navController.navigate("absen")
               }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(imageItems.size) { index ->
                val (image, route) = imageItems[index]
                RoundedImageCard(
                    drawableResId = image,
                    contentDescription = "Image $index",
                    text = route.capitalize(),
                    onClick = { navController.navigate(route) }
                )
            }
        }
    }
}
