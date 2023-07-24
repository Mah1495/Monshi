package com.example.callcenter.utils

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.callcenter.CallInfo
import com.example.callcenter.CallNotificationReceiver
import com.example.callcenter.IncomingChannel
import com.example.callcenter.OutGoingChannel
import com.example.callcenter.R
import com.example.callcenter.screens.call_screen.CallScreenActivity
import com.example.callcenter.screens.incoming_call.IncomingCallActivity
import javax.inject.Singleton


const val Incoming_Call_Notification_Id: Int = 123
const val Outgoing_Call_Notification_Id: Int = 321
const val Active_Call_Notification_Id: Int = 313
const val Incoming_Answer_Action: String = "Answer"
const val Incoming_Reject_Action: String = "Reject"

fun Context.cancelNotification(id: Int = Incoming_Call_Notification_Id) {
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
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
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


    fun activeCall(call: CallInfo) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClass(app, CallScreenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(app, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            app, OutGoingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_LOW

        // Set notification content intent to take user to the fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate.

        // Setup notification content.
        builder.setSmallIcon(R.drawable.ic_called)
        builder.setContentTitle("Call: " + call.displayName())

        // Use builder.addAction(..) to add buttons to answer or reject the call.
        val notificationManager = app.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(Active_Call_Notification_Id, builder.build())
    }

    fun outgoingCall(call: CallInfo) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClass(app, CallScreenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(app, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            app, OutGoingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_LOW

        // Set notification content intent to take user to the fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate.

        // Setup notification content.
        builder.setSmallIcon(R.drawable.ic_calling)
        builder.setContentTitle("Calling: " + call.displayName())

        // Use builder.addAction(..) to add buttons to answer or reject the call.
        val notificationManager = app.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(Outgoing_Call_Notification_Id, builder.build())
    }

    fun incoming(call: CallInfo) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION

        intent.setClass(app, IncomingCallActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(app, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            app, IncomingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH

        builder.setContentIntent(pendingIntent)
        builder.setFullScreenIntent(pendingIntent, true)

        // Setup notification content.
        builder.setSmallIcon(R.drawable.incoming_call)
        val answer = PendingIntent.getBroadcast(
            app,
            1,
            Intent(app, CallNotificationReceiver::class.java).setAction(Incoming_Answer_Action),
            PendingIntent.FLAG_MUTABLE
        )
        val reject = PendingIntent.getBroadcast(
            app,
            2,
            Intent(app, CallNotificationReceiver::class.java).setAction(Incoming_Reject_Action),
            PendingIntent.FLAG_MUTABLE
        )
        val contentView = RemoteViews(app.packageName, R.layout.incoming_notification)
        contentView.setOnClickPendingIntent(R.id.answer, answer)
        contentView.setOnClickPendingIntent(R.id.reject, reject)
        contentView.setTextViewText(R.id.notification_title, call.displayName())
        val contentViewEx = RemoteViews(app.packageName, R.layout.incoming_notification_expand)
        contentViewEx.setOnClickPendingIntent(R.id.answer, answer)
        contentViewEx.setOnClickPendingIntent(R.id.reject, reject)
        contentViewEx.setTextViewText(R.id.notification_title, call.displayName())
        contentViewEx.setTextViewText(R.id.notification_content, call.note)
        builder.setCustomContentView(contentView)
        builder.setCustomBigContentView(contentViewEx)
        val notificationManager = app.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(Incoming_Call_Notification_Id, builder.build())
    }

}
