package com.example.presensiapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.presensiapp.R
import com.example.presensiapp.models.Permit
import com.example.presensiapp.reusable.DashboardCard
import com.example.presensiapp.reusable.RoundedImageCard
import com.example.presensiapp.viewModel.PresensiViewModel
import com.example.presensiapp.viewModel.UserViewModel
import com.example.presensiapp.widgets.CustomContainer
import parseDateTime

@Composable
fun IzinScreen(navController: NavController) {
    var isNotificationEnabled by remember { mutableStateOf(false) }
    val presensiViewModel: PresensiViewModel = viewModel()
    val permitResponse by presensiViewModel.presensiResponse.collectAsState()
    var count = 0
    LaunchedEffect(Unit) {
        presensiViewModel.fetchPresensi()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0EADD)),
    ) {
        permitResponse?.countPermit.let {
            if (it != null) {
                count = it.toInt()
            }
        }
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
                    text = "Presensi",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            DashboardCard (
              "Riwayat Izin",
                count = count.toString(),
                onClick = {

                }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.
                padding(8.dp)
                ,
                fontWeight = FontWeight.Normal,
                text = "Riwayat",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                permitResponse?.permitData?.let { permitList ->
                    items(permitList.size) { index ->
                        val permit = permitList[index] // Ambil item pada indeks
                        val (tanggal, namaBulan) = parseDateTime(permit.datetime ?: "")
                        Log.d("dATA PERMIT", permit.datetime.toString())
                        // Item Composable untuk setiap data
//                        Text(
//                            text = "Tanggal:df, Bulan: sdf",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp)
//                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
//                                .padding(16.dp),
//                        )
//                        val (tanggal, namaBulan) = parseDateTime(datetime)
                        PermitItem(tanggal = tanggal, namaBulan = namaBulan, index,permit)
                    }
                }
            }
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
                },
                drawableIcon = R.drawable.ic_launcher_foreground,
                isIconEnabled = true

            )
            Spacer(Modifier.height(8.dp))

            CustomContainer(
                title = "Laporan Kinerja",
                onClick = {

                },
                drawableIcon = R.drawable.ic_launcher_foreground,
                isIconEnabled = true
            )
        }

    }
}

@Composable
fun PermitItem(tanggal: String, namaBulan: String, index: Int, permit: Permit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box (
            modifier = Modifier
                .background(Color(0xFFF0EADD))
                .padding(8.dp)
                .size(50.dp)
        ){

            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = tanggal,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign =  TextAlign.Center
                )
                Text(
                    text = namaBulan,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
        Text(
            text = "${permit.description}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .padding(4.dp)
                 .background(Color(0xFFF0EADD)
                ),

        ){
//           if(index % 2 == 0) {
//               Text(
//                   text = "Diizinkan",
//                   style = MaterialTheme.typography.bodyMedium,
//                   fontWeight = FontWeight.Bold,
//                   color = Color(0xFF50C345)
//               )
//           }else{
//               Text(
//                   text = "Ditolak",
//                   style = MaterialTheme.typography.bodyMedium,
//                   fontWeight = FontWeight.Bold,
//                   color = Color(0xFF50C345)
//               )
//           }

        }
    }
}