import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*
@Composable
fun DateTimePickerComposable(onDateTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // DatePicker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Format tanggal menjadi "YYYY-MM-DD"
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    // TimePicker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)

            val dateTimeString = "$selectedDate $selectedTime:00.000"
            onDateTimeSelected(dateTimeString)
        },
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE],
        true
    )

    Column(modifier = Modifier.padding(2.dp)) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B9C8B),
                contentColor = Color.White
            ),
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pilih Tanggal")
        }

        Spacer(modifier = Modifier.height(2.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B9C8B),
                contentColor = Color.White
            ),
            onClick = { timePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pilih Waktu")
        }

        Spacer(modifier = Modifier.height(2.dp))

        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            Text(
                text = "Tanggal & Waktu: $selectedDate $selectedTime:00.000",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}