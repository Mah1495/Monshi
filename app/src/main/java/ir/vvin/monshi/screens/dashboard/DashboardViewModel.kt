package ir.vvin.monshi.screens.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.provider.CallLog.Calls.LIMIT_PARAM_KEY
import android.provider.CallLog.Calls.OFFSET_PARAM_KEY
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.vvin.monshi.entities.CallLog
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

object Constants {
    val cols = arrayOf("name", "number", "date", "type")
}

fun Date.format(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(this)
}

@HiltViewModel
class DashboardViewModel @Inject constructor(val app: Application) : ViewModel() {

    @SuppressLint("Range")
    fun getCalls() {
        val cursor = app.contentResolver.query(
            android.provider.CallLog.Calls.CONTENT_URI.buildUpon()
                .appendQueryParameter(LIMIT_PARAM_KEY, "20")
                .appendQueryParameter(OFFSET_PARAM_KEY, "10").build(),
            Constants.cols,
            null,
            null,
            null
        )
//        var i = 0
//        cursor?.columnNames?.forEach {
//            _logs.add(CallLog(0, "", "$i $it", Date(), ""))
//            i++
//        }

        while (cursor?.moveToNext() == true) {
            cursor.columnNames.forEach {
                val ind = cursor.getColumnIndex(it)


                if (ind > -1) _logs.add(
                    CallLog(
                        0,
                        it,
                        cursor.getString(ind) ?: "null",
                        if (it == Constants.cols[2]) Date(cursor.getLong(ind)) else Date(),
                        ""
                    )
                )
            }
            break
        }
    }

    private val cursor = app.contentResolver.query(
        android.provider.CallLog.Calls.CONTENT_URI.buildUpon()
            .appendQueryParameter(LIMIT_PARAM_KEY, "1").build(), null, null, null, null
    );
    private var _logs: MutableList<CallLog> = listOf<CallLog>().toMutableList()
    val logs: List<CallLog> = _logs
}

