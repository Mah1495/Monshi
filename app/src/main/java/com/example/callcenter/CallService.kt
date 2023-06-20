package com.example.callcenter

import android.app.NotificationManager
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import androidx.core.content.getSystemService
import com.example.callcenter.utils.Active_Call_Notification_Id
import com.example.callcenter.utils.AppNotificationManager
import com.example.callcenter.utils.Incoming_Call_Notification_Id
import com.example.callcenter.utils.Outgoing_Call_Notification_Id
import com.example.callcenter.utils.cancelNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CallService : InCallService() {

    @Inject
    lateinit var model: CallHandler

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        model.call = call
        model.state.subscribe {
            if (it != Call.STATE_RINGING) {
                this.applicationContext.cancelNotification()
            }
            if (it == Call.STATE_ACTIVE && model.incoming) {
                this.notificationManager.activeCall(call)
            }
            if (it == Call.STATE_DISCONNECTED) {
                this.cancelNotification(Active_Call_Notification_Id)
            }
        }
    }

    override fun onCallRemoved(call: Call?) {
        applicationContext.getSystemService<NotificationManager>()?.cancel(
            Incoming_Call_Notification_Id
        )
        applicationContext.getSystemService<NotificationManager>()?.cancel(
            Outgoing_Call_Notification_Id
        )
        model.call = null
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState?) {
        super.onCallAudioStateChanged(audioState)
    }
}