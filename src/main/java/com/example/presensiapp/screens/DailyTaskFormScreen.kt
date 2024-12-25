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
    import com.example.presensiapp.viewModel.TaskViewModel
    import com.example.presensiapp.viewModel.UserViewModel
    import com.google.accompanist.permissions.ExperimentalPermissionsApi
    import com.google.accompanist.permissions.isGranted
    import com.google.accompanist.permissions.rememberPermissionState
    import com.google.gson.Gson
    import kotlinx.coroutines.flow.firstOrNull
    import okhttp3.MediaType
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody
    import java.io.ByteArrayOutputStream


    @Composable
    @OptIn(ExperimentalPermissionsApi::class)
    fun DailyTaskFormScreen(navController: NavController, viewModel: TaskViewModel,taskId: Int) {
        val isLoading by viewModel.isLoading.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()

        // State variables for both images
        var image1Bitmap by remember { mutableStateOf<Bitmap?>(null) }
        var image2Bitmap by remember { mutableStateOf<Bitmap?>(null) }

        val context = LocalContext.current
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        // Launchers for the two camera inputs
        val cameraLauncher1 = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let { image1Bitmap = it }
            }
        }

        val cameraLauncher2 = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let { image2Bitmap = it }
            }
        }


        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            Text(text = "Task ID: $taskId")

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
                    text = "Tugas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }

            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = "Ambil foto Task 1",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            CameraCard(
                bitmap = image1Bitmap,
                onClick = {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher1.launch(cameraIntent)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = "Ambil foto Task 2",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            CameraCard(
                bitmap = image2Bitmap,
                onClick = {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher2.launch(cameraIntent)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                    if (image1Bitmap == null || image2Bitmap == null) {
                        Toast.makeText(context, "Harap masukkan kedua foto!", Toast.LENGTH_SHORT).show()
                        return@RoundedButton
                    }

                    // Convert both images to MultipartBody
                    val imagePart1 = bitmapToMultipartBodyPart(image1Bitmap, "image1")
                    val imagePart2 = bitmapToMultipartBodyPart(image2Bitmap, "image2")

                    // Submit to ViewModel
                    viewModel.submitDailyTask(
                        imagePart1,
                        imagePart2,
                        taskId,
                        onSuccess = { onSuccess(context) }
                    )
                },
                modifier = Modifier.fillMaxWidth() // Make the button fill the width
            )
        }
    }

    private fun onSuccess(context: Context) {
        Toast.makeText(
            context,
            "Task berhasil dikirim!",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun bitmapToMultipartBodyPart(bitmap: Bitmap?, name: String): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val requestBody = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(), byteArrayOutputStream.toByteArray()
        )
        return MultipartBody.Part.createFormData(name, "image.jpg", requestBody)
    }
