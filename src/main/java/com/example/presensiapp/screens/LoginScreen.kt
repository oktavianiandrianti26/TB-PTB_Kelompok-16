package com.example.presensiapp.screens

import android.util.Log
import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.presensiapp.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presensiapp.viewModel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.presensiapp.models.User
import com.example.presensiapp.widgets.CustomRoundedButton
import com.example.presensiapp.widgets.RoundedTextField

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val navController = rememberNavController()

    val userViewModel: UserViewModel = viewModel()
    val isLoading by userViewModel.isLoading.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    val loginResponse by userViewModel.loginResponse.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login logo",
                modifier = Modifier.fillMaxWidth().height(400.dp)
            )
        }

        // Content Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color(0xFF739072),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "LOGIN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start),
                    color = Color.White
                )
                Text(
                    text = "Login to your account",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Start),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                RoundedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )
                Spacer(modifier = Modifier.height(16.dp))

                RoundedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password"
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomRoundedButton(
                    text = "Sign In",
                    onClick = {
                        userViewModel.login(email.trim(), password.trim(), {
                            onLoginSuccess()
                        })

                    },
                    backgroundColor = Color(0xFF3A4D39),
                    contentColor = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
                    onClick = {
                        onRegister()                    }
                ) {
                    Text("Don't have an account? Register")
                }
            }
        }

        // Full-Screen Loading Overlay
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
                onDismissRequest = { userViewModel.clearError() },
                title = { Text(text = "Error", color = Color.Red) },
                text = { Text(text = errorMessage ?: "Unknown error") },
                confirmButton = {
                    TextButton(onClick = { userViewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

