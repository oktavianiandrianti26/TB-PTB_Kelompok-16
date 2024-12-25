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
    fun AbsenScreen(navController: NavController, viewModel: PresensiViewModel) {
        val nama = remember { mutableStateOf("") }
        var tanggal by remember { mutableStateOf("") }
        val lokasi = remember { mutableStateOf("") }
        val keterangan = remember { mutableStateOf("") }

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
                    text = "Presensi",
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

            // Input Lokasi
            CustomRoundedTextField(
                value = lokasi.value,
                onValueChange = { lokasi.value = it },
                label = "Lokasi",
                placeholder = "Masukkan lokasi"
            )

            // Input Keterangan
            RoundedTextArea(
                value = keterangan.value,
                onValueChange = { keterangan.value = it },
                label = "Deskripsi",
                placeholder = "Masukkan deskripsi"
            )


            // Tombol Kirim Data
            RoundedButton(
                text = "Ambil Foto",
                onClick = {
                    if (nama.value.isEmpty() || tanggal.isEmpty() || lokasi.value.isEmpty()) {
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

//
                    val absenRequestJson = Gson().toJson(absenRequest)

                    val route = "takecamera/${absenRequestJson}"
                    navController.navigate(route)

                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    @OptIn(ExperimentalPermissionsApi::class)
    fun TakeCamera(navController: NavController,absenRequest: AbsenRequest?,backStackEntry: NavBackStackEntry,viewModel: PresensiViewModel) {
        val isLoading by viewModel.isLoading.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()

        var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let { imageBitmap = it }
            }
        }
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .size(24.dp)
                        .padding(end = 8.dp),
                    tint = Color.Black
                )

                Text(
                    text = "Absen",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }


            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = "Ambil foto selfie",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            CameraCard(
                bitmap = imageBitmap,
                onClick = {
//                    if (cameraPermissionState.status.isGranted) {
//                        openCamera(cameraLauncher)
//                    } else {
//                        cameraPermissionState.launchPermissionRequest()
//                    }

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))


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
                text = "Kirim",
                onClick = {
                    if (imageBitmap == null) {
                        Toast.makeText(context, "Harap Masukkan foto semua data!", Toast.LENGTH_SHORT).show()
                        return@RoundedButton
                    }

                    val nameRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.name) }
                    val datetimeRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.datetime) }
                    val locationRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.location) }
                    val descriptionRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.description) }
                    val typeRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.type) }
                    val userIdRequestBody =
                        absenRequest?.let { RequestBody.create(MultipartBody.FORM, it.userId) }

                    imageBitmap?.let { bitmap ->
                        val imagePart = bitmapToMultipartBodyPart(bitmap, "image")

                        viewModel.submitAbsen(
                                                nameRequestBody!!,
                                                datetimeRequestBody!!,
                                                locationRequestBody!!,
                                                descriptionRequestBody!!,
                                                typeRequestBody!!,
                                                userIdRequestBody!!,
                                                imagePart,
                            {
                                onSuccess(context)
                            }
                                            )


                                        }

                }, modifier = Modifier.fillMaxWidth() // Menjadikan tombol memenuhi lebar maksimum

            )


        }


    }

    private fun openCamera(cameraLauncher: androidx.activity.result.ActivityResultLauncher<Intent>) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }
    private fun onSuccess(context: Context){
        Toast.makeText(
            context,
            "Absen berhasil dikirim!",
            Toast.LENGTH_SHORT
        ).show()
    }