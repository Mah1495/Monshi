package com.example.callcenter.screens.recent_call

import android.app.Application
import android.provider.CallLog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.PhoneMissed
import androidx.compose.material.icons.filled.Voicemail
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.callcenter.utils.call
import com.example.callcenter.utils.sms
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

enum class CallType(val type: Int, val icon: ImageVector) {
    Incoming(1, Icons.Default.CallReceived),
    Outgoing(2, Icons.Default.CallMade),
    Missed(3, Icons.Default.CallMissed),
    VoiceMail(4, Icons.Default.Voicemail),
    Reject(5, Icons.Default.PhoneMissed),
    Block(6, Icons.Default.Block),
    Answered(7, Icons.Default.CallEnd);

    companion object {
        fun fromInt(type: Int) = values()[type - 1]
    }
}

@HiltViewModel
class RecentCallViewModel @Inject constructor(
    val app: Application
) : ViewModel() {
    private val projection = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.CACHED_PHOTO_URI,
        CallLog.Calls.CACHED_NAME
    )

    fun logs(): List<RecentCall> {
        val cursor = app.contentResolver.query(
            CallLog.Calls.CONTENT_URI.buildUpon()
                .build(),
            projection, null, null, "date DESC"
        )
        val list: MutableList<RecentCall> = mutableListOf()
        while (cursor != null && cursor.moveToNext()) {
            val recentCall = RecentCall(
                cursor.getString(6) ?: cursor.getString(1) ?: "noname",
                Date(cursor.getLong(3)).toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
                CallType.fromInt(cursor.getInt(2)).icon,
                cursor.getString(1) ?: "noname"
            )
            list.add(recentCall)
        }
        return list
    }

    fun onEvent(event: CallsEvent) {
        when (event) {
            is CallsEvent.Call -> {
                app.call(event.number)
            }

            is CallsEvent.Message -> {
                app.sms(event.number)
            }

            is CallsEvent.Info -> {

            }

            is CallsEvent.Notes -> {

            }

            else -> {

            }
        }
    }
}

sealed class CallsEvent {
    data class Call(val number: String) : CallsEvent()
    data class Message(val number: String) : CallsEvent()
    data class Info(val number: String) : CallsEvent()
    data class Notes(val number: String) : CallsEvent()
}