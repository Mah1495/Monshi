package ir.vvin.monshi

import android.app.NotificationManager
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import ir.vvin.monshi.utils.Incoming_Call_Notification_Id
import ir.vvin.monshi.utils.Outgoing_Call_Notification_Id
import javax.inject.Inject


@AndroidEntryPoint
class CallService : InCallService() {

    @Inject
    lateinit var model: CallHandler

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        setAudioRoute(CallAudioState.ROUTE_EARPIECE)
        model.service = this
        model.call = call
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