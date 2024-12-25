package com.example.presensiapp

import ReportScreen
import TaskReportScreen
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.presensiapp.ui.theme.PresensiAppTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.presensiapp.navigation.CustomBottomNavigation
import com.example.presensiapp.screens.AbsenScreen
import com.example.presensiapp.screens.HomeScreen
import com.example.presensiapp.screens.LoginScreen
import com.example.presensiapp.screens.NotificationScreen
import com.example.presensiapp.screens.ProfileScreen
import com.example.presensiapp.screens.RegisterScreen
import com.example.presensiapp.screens.SplashScreen
import com.example.presensiapp.screens.TakeCamera
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.presensiapp.datastore.DataStoreManager
import com.example.presensiapp.models.AbsenRequest
import com.example.presensiapp.screens.DailyTaskFormScreen
import com.example.presensiapp.screens.DailyTaskScreen
import com.example.presensiapp.screens.EditProfileScreen
import com.example.presensiapp.screens.InventoryScreen
import com.example.presensiapp.screens.IzinFormScreen
import com.example.presensiapp.screens.IzinScreen
import com.example.presensiapp.screens.RiwayatScreen
import com.example.presensiapp.screens.TakeImageInventory
import com.example.presensiapp.screens.WebViewScreen
import com.example.presensiapp.viewModel.InventoryViewModel
import com.example.presensiapp.viewModel.PresensiViewModel
import com.example.presensiapp.viewModel.TaskViewModel
import com.example.presensiapp.viewModel.UserViewModel
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    MainScreen() // Memanggil MainScreen di sini
                }
            }
        }
    }
}


//val imageItems = listOf(
//    R.drawable.tugas to "Tugas",
//    R.drawable.izin to "Izin",
//    R.drawable.riwayat to "Riwayat",
//    R.drawable.inventaris to "Inventaris"
//)

sealed class Screen(val route: String) {
    object Tugas : Screen("tugas")
    object Izin : Screen("izin")
    object Riwayat : Screen("riwayat")
    object Inventaris : Screen("inventory")
}

val imageItems = listOf(
    R.drawable.tugas to Screen.Tugas.route,
    R.drawable.izin to Screen.Izin.route,
    R.drawable.riwayat to Screen.Riwayat.route,
    R.drawable.inventaris to Screen.Inventaris.route
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screensWithoutBottomNav = listOf("recapitulation","splash", "login", "register", "takecamera")
    val userViewModel: UserViewModel = viewModel()
    val presensiViewModel: PresensiViewModel = viewModel()
    val dailyTaskViewModel: TaskViewModel = viewModel()
    val inventoryViewModel: InventoryViewModel = viewModel()

    Scaffold(
        containerColor = Color(0xFFF0EADD),
        bottomBar = {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentDestination !in screensWithoutBottomNav) {
                CustomBottomNavigation(navController)
            }        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = "login") {
                composable("recapitulation") {
                    val context = LocalContext.current
                    val dataStoreManager = DataStoreManager(context)

                    val email by dataStoreManager.userEmail.collectAsState(initial = null)

                    if (email != null) {
                        WebViewScreen(url = "http://172.20.10.4:3000/api/recapitulation?email=$email")
                    } else {
                        Text(text = "Loading...")
                    }
                }
                composable("splash") { SplashScreen { navController.navigate("login") } }
                composable("login") { LoginScreen ({
                    navController.navigate("home")
                }, {
                    navController.navigate("register")
                }) }
                composable("register") { RegisterScreen ({
                    navController.navigate("home")

                }, {
                    navController.navigate("login")

                }) }
                composable("home") { HomeScreen(imageItems, navController) }
                composable("notification") { NotificationScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("editProfile") { EditProfileScreen(navController,userViewModel) }

                composable(
                    "takecamera/{absenRequest}",
                    arguments = listOf(
                        navArgument("absenRequest") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
//                    val absenRequest = backStackEntry.arguments?.getParcelable<AbsenRequest>("absenRequest")
                    val absenAsJson = backStackEntry.arguments?.getString("absenRequest")
                    val absen = Gson().fromJson(absenAsJson, AbsenRequest::class.java)
                    TakeCamera(navController,absen,backStackEntry,presensiViewModel)
                }

                composable(
                    "takeimageinventory/{inventoryRequest}",
                    arguments = listOf(
                        navArgument("inventoryRequest") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val inventoryAsJson = backStackEntry.arguments?.getString("inventoryRequest")
                    val inventory = Gson().fromJson(inventoryAsJson, AbsenRequest::class.java)
                    TakeImageInventory(navController,inventory,backStackEntry,inventoryViewModel)
                }
                composable("inventory") { InventoryScreen(navController, inventoryViewModel) }
                composable("absen") { AbsenScreen(navController,presensiViewModel) }
                composable(Screen.Izin.route) { IzinFormScreen(navController,presensiViewModel) }

                composable(Screen.Tugas.route) { DailyTaskScreen(navController) }
                composable("performance") { TaskReportScreen(navController) }

                composable("dailytaskform/{id}",arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                        backStackEntry ->
                    val taskId = backStackEntry.arguments?.getInt("id") ?: 0
                    DailyTaskFormScreen(navController,dailyTaskViewModel, taskId) }

                composable("izinrecap") { IzinScreen(navController) }
                composable("riwayat") { RiwayatScreen(navController) }
//                composable(Screen.Inventaris.route) { InventarisScreen() }
            }
        }
    }}







