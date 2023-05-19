package com.example.callcenter

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.CallAudioState
import android.telecom.InCallService
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class CallService : InCallService() {
    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        OngoingCall.call = call
        if (call.details.callDirection == DIRECTION_INCOMING)
            incoming(call)
        else
            outgoing(call)
    }

    private fun outgoing(call: Call) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.data = call.details.handle
        intent.setClass(applicationContext, CallScreenActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            applicationContext,
            IncomingChannel
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
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(123, builder.build())
        val nin = Intent(Intent.ACTION_MAIN, null)
        nin.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        nin.data = call.details.handle
        nin.setClass(applicationContext, CallScreenActivity::class.java)
        startActivity(nin)
    }

    private fun incoming(call: Call) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.data = call.details.handle
        intent.setClass(applicationContext, CallIncomingActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 123, intent, PendingIntent.FLAG_MUTABLE)

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = NotificationCompat.Builder(
            applicationContext,
            IncomingChannel
        )
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH

        // Set notification content intent to take user to the fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate

        // Setup notification content.
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle(call.details.handle.schemeSpecificPart)
        builder.setContentText("incoming")

        // Use builder.addAction(..) to add buttons to answer or reject the call.
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(123, builder.build())
    }

    override fun onCallRemoved(call: Call?) {
        val localBroadcastManager = LocalBroadcastManager
            .getInstance(this);
        localBroadcastManager.sendBroadcast(
            Intent(Close_Call)
        )
        OngoingCall.call = null
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState?) {
        super.onCallAudioStateChanged(audioState)
    }

}