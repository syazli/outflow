package my.com.syazli.outflow.utils

import android.icu.text.SimpleDateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

object FormatHelper {

    fun formatAmount(amount: Double): String {
        val format = NumberFormat.getNumberInstance(Locale("ms", "MY"))
        format.minimumFractionDigits = 2
        format.maximumFractionDigits = 2
        return "RM ${format.format(amount)}"
    }

    fun formatDate(timeStamp: Long): String {
        val sdf = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return sdf.format(Date(timeStamp))
    }
}

