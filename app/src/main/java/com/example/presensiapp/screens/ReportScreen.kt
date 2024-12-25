import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.presensiapp.R
import com.example.presensiapp.helper.CurrentDateDisplay
import com.example.presensiapp.reusable.DashboardCard
import com.example.presensiapp.screens.PermitItem
import com.example.presensiapp.viewModel.PresensiViewModel
import com.example.presensiapp.widgets.CustomContainer

@Composable
fun TaskReportScreen(navController: NavController) {
    val viewModel: PresensiViewModel = viewModel()

    val taskReport by viewModel.taskReport.collectAsState()

    // Trigger data fetch
    LaunchedEffect(Unit) {
        viewModel.fetchTaskReport()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (taskReport == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Laporan Kinerja",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tanggal pelaksanaan:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        CurrentDateDisplay()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Deskripsi Tugas:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                taskReport?.reportData?.forEach { task ->
                    Text(
                        text = "- ${task.description}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReportScreen(navController: NavController) {
    var isNotificationEnabled by remember { mutableStateOf(false) }
    val presensiViewModel: PresensiViewModel = viewModel()
    val permitResponse by presensiViewModel.presensiResponse.collectAsState()
    var count = 0
//    LaunchedEffect(Unit) {
//        presensiViewModel.fetchPresensi()
//    }
//

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
