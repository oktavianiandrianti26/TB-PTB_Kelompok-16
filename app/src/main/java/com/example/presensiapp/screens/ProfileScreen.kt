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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.presensiapp.datastore.DataStoreManager
import com.example.presensiapp.helper.bitmapToMultipartBodyPart
import com.example.presensiapp.helper.toBase64
import com.example.presensiapp.models.AbsenRequest
import com.example.presensiapp.models.User
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.prefs.Preferences

@Composable
fun ProfileScreen(navController: NavController) {
     lateinit var dataStoreManager: DataStoreManager

    val userViewModel: UserViewModel = viewModel()
        val userDataLocalStorage = userViewModel.userDataLocalStorage.collectAsState(initial = User(
        name = "",
        hp = "",
        created_at = "",
        image = "",
        email = "",
        id = 0,
        password = "",
        address = ""
    )
        )
    val context = LocalContext.current

    val dataStoreManagers = DataStoreManager(context)

    if(userDataLocalStorage.value?.id != 0){

        userDataLocalStorage.let {
                Log.d("FULL DATA",userDataLocalStorage.value.toString())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFCAD2C5))
                .padding(16.dp)
        ) {


            if(userDataLocalStorage.value?.image != null|| userDataLocalStorage.value?.image   != ""){
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .background(Color.Gray), // Fallback color while loading
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "http://172.20.10.3:3000/uploads/1734756862417.jpg",
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }else{
                Box(
                modifier = Modifier
                    .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for profile image
                    }
            }

//
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("editProfile") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Edit Profile")
            }
            Spacer(modifier = Modifier.height(32.dp))
            userDataLocalStorage.value?.let { ProfileInfoSection("Nama", it.name) }
            userDataLocalStorage.value?.let { ProfileInfoSection("No. Telepon", it.hp) }
            userDataLocalStorage.value?.let { ProfileInfoSection("Alamat", it.address) }
            Spacer(modifier = Modifier.height(16.dp))
            PreferencesSection(navController, dataStoreManagers)
        }
    }


}

@Composable
fun ProfileInfoSection(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label :", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, style = MaterialTheme.typography.bodySmall)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun PreferencesSection(navController: NavController, dataStoreManager: DataStoreManager) {
    var context= LocalContext.current
    Text(
        text = "Preferences",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = Color.DarkGray
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to Language Selection */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Sign out",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Sign out",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                // Clear preferences and redirect
                handleLogout(context = context, dataStoreManager, navController)
            }
        )
    }
}
fun handleLogout(context: Context, dataStoreManager: DataStoreManager, navController: NavController) {
    CoroutineScope(Dispatchers.IO).launch {
        // Clear preferences
        dataStoreManager.clearPreferences()

        withContext(Dispatchers.Main) {

            navController.navigate("login")
        }
    }
}

@Composable
fun EditProfileScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val userViewModel: UserViewModel = viewModel()


        val userDataLocalStorage = userViewModel.userDataLocalStorage.collectAsState(initial = User(
        name = "",
        hp = "",
        created_at = "",
        image = "",
        email = "",
        id = 0,
        password = "",
        address = ""
    ))



    val context = LocalContext.current
    var name by remember { mutableStateOf(userDataLocalStorage.value?.name ?: "") }
    var phone by remember { mutableStateOf(userDataLocalStorage.value?.hp ?: "") }
    var address by remember { mutableStateOf(userDataLocalStorage.value?.address ?: "") }
    var imageUri by remember { mutableStateOf(userDataLocalStorage.value?.image ?: "") }

    // Update state when userDataLocalStorage changes
    LaunchedEffect(userDataLocalStorage) {
        name = userDataLocalStorage.value!!.name
        phone = userDataLocalStorage.value!!.hp
        address = userDataLocalStorage.value!!.address
        imageUri = userDataLocalStorage.value!!.image
    }




    // Launcher for selecting an image
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri.toString()
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCAD2C5))
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(bottom = 16.dp)
        )
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Image Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            val profileImage = imageUri ?: userDataLocalStorage.value?.image
            if (profileImage != null) {
                if (profileImage.isNotEmpty()) {
                    AsyncImage(
                        model = profileImage,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = "No Image", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Change Picture Button
        Text(
            text = "Change Picture",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { launcher.launch("image/*") },
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextFields for editing profile details
        CustomTextField(label = "Nama", text = name, onTextChanged = { name = it })
        CustomTextField(label = "Nomor Telepon", text = phone, onTextChanged = { phone = it })
        CustomTextField(label = "Alamat", text = address, onTextChanged = { address = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Update button
        Button(
            onClick = {
                // Update data in ViewModel or send to API
                userViewModel.updateUser(
                    User(
                        name = name,
                        hp = phone,
                        address = address,
                        id = userDataLocalStorage.value!!.id,
                        email = userDataLocalStorage.value!!.email,
                        image = imageUri ?: userDataLocalStorage.value!!.image,
                        created_at = userDataLocalStorage.value!!.created_at,
                        password = userDataLocalStorage.value!!.password
                    )
                )
//                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCAD2C5))
        ) {
            Text(text = "Update")
        }
    }
}

@Composable
fun CustomTextField(label: String, text: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onTextChanged,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(8.dp))
}


