import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

fun parseDateTime(datetime: String): Pair<String, String> {
    return try {
        val parsedDateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ISO_DATE_TIME)
        val tanggal = parsedDateTime.dayOfMonth.toString()
        val namaBulan = parsedDateTime.month.getDisplayName(org.threeten.bp.format.TextStyle.FULL, Locale.getDefault())
        tanggal to namaBulan
    } catch (e: Exception) {
        "" to ""
    }
}
