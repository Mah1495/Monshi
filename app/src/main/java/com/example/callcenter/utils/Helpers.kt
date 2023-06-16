package com.example.callcenter.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


fun CharSequence.removeLast(): CharSequence {
    if (!this.isNullOrEmpty()) {
        return this.substring(0, this.length - 1)
    }
    return this
}

val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
fun LocalDateTime.getDate(): String {
    val diff = ChronoUnit.DAYS.between(this, LocalDateTime.now()).toInt()
    if (diff == 0) {
        return "Today"
    }
    if (diff == 1) {
        return "yesterday"
    }
    return this.format(dateFormatter)
}

fun LocalDateTime.getTime(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))
}

fun LocalDateTime.isSameAs(date: LocalDateTime): Boolean {
    return this.getDate() == date.getDate()
}

@SuppressLint("MissingPermission")
fun Application.call(number: String) {
    val tm = this.getSystemService(Activity.TELECOM_SERVICE) as TelecomManager
    val uri = Uri.fromParts("tel", number, null)
    val extras = Bundle()
    extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
    tm.placeCall(uri, extras)
}

fun Application.sms(number: String) {
    val sendIntent = Intent(Intent.ACTION_VIEW)
    sendIntent.data = Uri.parse("sms:$number")
    sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    this.startActivity(sendIntent)
}