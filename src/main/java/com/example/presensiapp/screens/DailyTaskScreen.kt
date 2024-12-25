@file:JvmName("NotificationScreensKt")

package com.example.presensiapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.presensiapp.R
import com.example.presensiapp.helper.CurrentDateDisplay
import com.example.presensiapp.helper.CustomTaskCard
import com.example.presensiapp.viewModel.PresensiViewModel
import com.example.presensiapp.viewModel.TaskViewModel
import com.example.presensiapp.widgets.CustomContainer

@Composable
fun DailyTaskScreen(navController: NavController) {

    val taskViewModel: TaskViewModel = viewModel()
    val taskResponse by taskViewModel.taskResponse.collectAsState()
    var isNotificationEnabled by remember { mutableStateOf(false) }
    val isLoading by taskViewModel.isLoading.collectAsState()
    val errorMessage by taskViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.fetchTask()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0EADD)),
    ) {

        LazyColumn(
        ) {

            item {
                Box(Modifier
                    .padding(16.dp)
                ){
                    Column {
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .clickable { navController.popBackStack() }
                                    .padding(bottom = 10.dp)
                            )
                            Text(
                                text = "Daily Task",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CurrentDateDisplay()
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                        )
                    }

                }

                taskResponse.forEach { task ->
                    if (task != null) {
                        CustomTaskCard(task.description, task.location, task.time, task.id, navController)
                    }
                }

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
                        onDismissRequest = { taskViewModel.clearError() },
                        title = { Text(text = "Error", color = Color.Red) },
                        text = { Text(text = errorMessage ?: "Unknown error") },
                        confirmButton = {
                            TextButton(onClick = { taskViewModel.clearError() }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }

        }
    }
}

