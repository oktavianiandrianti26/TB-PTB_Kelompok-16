    package com.example.presensiapp.screens

    import DateTimePickerComposable
    import android.Manifest
    import android.app.Activity
    import android.content.Context
    import android.content.Intent
    import android.graphics.Bitmap
    import android.os.Bundle
    import android.provider.MediaStore
    import android.util.Log
    import android.widget.Toast
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
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
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.unit.dp
    import androidx.core.os.bundleOf
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavBackStackEntry
    import androidx.navigation.NavController
    import com.example.presensiapp.helper.bitmapToMultipartBodyPart
    import com.example.presensiapp.helper.toBase64
    import com.example.presensiapp.models.AbsenRequest
    import com.example.presensiapp.reusable.CameraCard
    import com.example.presensiapp.reusable.CustomRoundedTextField
    import com.example.presensiapp.reusable.RoundedButton
    import com.example.presensiapp.reusable.RoundedTextArea
    import com.example.presensiapp.viewModel.PresensiViewModel
    import com.example.presensiapp.viewModel.UserViewModel
    import com.google.accompanist.permissions.ExperimentalPermissionsApi
    import com.google.accompanist.permissions.isGranted
    import com.google.accompanist.permissions.rememberPermissionState
    import com.google.gson.Gson
    import kotlinx.coroutines.flow.firstOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody


    @Composable
    fun IzinFormScreen(navController: NavController, viewModel: PresensiViewModel) {
        val nama = remember { mutableStateOf("") }
        var tanggal by remember { mutableStateOf("") }
        val lokasi = remember { mutableStateOf("") }
        val keterangan = remember { mutableStateOf("") }
        val isLoading by viewModel.isLoading.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()

        val context = LocalContext.current



        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Input Nama

            Row(modifier = Modifier.align(Alignment.Start)){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(bottom = 10.dp)
                )
                Text(
                    text = "Pengajuan Izin",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomRoundedTextField(
                value = nama.value,
                onValueChange = { nama.value = it },
                label = "Nama Anda",
                placeholder = "Masukkan nama anda"
            )

            // Input Tanggal & Waktu
            DateTimePickerComposable { dateTime ->
                tanggal = dateTime // Simpan hasil yang dipilih oleh pengguna
            }


            // Input Keterangan
            RoundedTextArea(
                value = keterangan.value,
                onValueChange = { keterangan.value = it },
                label = "Deskripsi",
                placeholder = "Masukkan deskripsi"
            )



            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)), // Semi-transparent overlay
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text(text = "Error", color = Color.Red) },
                    text = { Text(text = errorMessage ?: "Unknown error") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }

            RoundedButton(
                text = "Kirim Data",
                onClick = {
                    if (nama.value.isEmpty() || tanggal.isEmpty() || keterangan.value.isEmpty()) {
                        Toast.makeText(context, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show()
                        return@RoundedButton
                    }

//
                        val absenRequest = AbsenRequest(
                            name = nama.value,
                            datetime = tanggal,
                            location = lokasi.value,
                            description = keterangan.value,
                            image = "base64Image",
                            type = "1",
                            userId = "123" // Contoh userId
                        )



                    viewModel.submitIzin(
                        nama.value,
                        tanggal,
                        keterangan.value,
                             "2",
                            "1",
                            {
                                onSuccess(context)
                            }
                        )                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }



    private fun onSuccess(context: Context){
        Toast.makeText(
            context,
            "Absen berhasil dikirim!",
            Toast.LENGTH_SHORT
        ).show()
    }

