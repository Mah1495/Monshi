package com.example.callcenter.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.telecom.Call
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.callcenter.IncomingChannel
import com.example.callcenter.OutGoingChannel
import com.example.callcenter.R
import com.example.callcenter.screens.call_screen.CallScreenActivity
import com.example.callcenter.screens.incoming_call.CallIncomingActivity
import javax.inject.Singleton

const val Incoming_Call_Notification_Id: Int = 123
const val Outgoing_Call_Notification_Id: Int = 321

fun Activity.cancelNotification(id: Int = Incoming_Call_Notification_Id) {
    this.getSystemService<NotificationManager>()?.cancel(id)
}

@Singleton
class AppNotificationManager(val app: Application) {

    @SuppressLint("WrongConstant")
    fun createIncomingChannel() {
        val channel = NotificationChannel(
            IncomingChannel, "Calls", NotificationManager.IMPORTANCE_MAX
        )
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        channel.setSound(
            ringtoneUri,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )

        val mgr = app.getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(channel)
    }

    @SuppressLint("WrongConstant")
    fun createOutgoingChannel() {
        val channel = NotificationChannel(
            OutGoingChannel, "Calls", NotificationManager.IMPORTANCE_DEFAULT
        )

        val mgr = app.getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(channel)
    }


    fun outgoingCall(call: Call) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = call.details.handle
        intent.setClass(app, CallScreenActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(app, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            app,
            OutGoingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_LOW

        // Set notification content intent to take user to the fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate.

        // Setup notification content.
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle(call.details.handle.schemeSpecificPart)
        builder.setContentText("outgoing")

        // Use builder.addAction(..) to add buttons to answer or reject the call.
        val notificationManager = app.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(Outgoing_Call_Notification_Id, builder.build())
    }

    fun incoming(call: Call) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
        intent.data = call.details.handle

        intent.setClass(app, CallIncomingActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(app, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            app,
            IncomingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH

        // Set notification content intent to take user to the fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        builder.setFullScreenIntent(pendingIntent, true)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate

        // Setup notification content.
        builder.setSmallIcon(R.drawable.incoming_call)
        builder.setContentTitle(call.details.handle.schemeSpecificPart)
        builder.setContentText("incoming")

        // Use builder.addAction(..) to add buttons to answer or reject the call.
        val notificationManager = app.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(Incoming_Call_Notification_Id, builder.build())
    }
}
