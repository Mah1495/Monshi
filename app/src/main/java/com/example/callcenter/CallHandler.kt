package com.example.callcenter

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.VideoProfile
import android.util.Log
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.ContactRepository
import com.example.callcenter.entities.Note
import com.example.callcenter.screens.call_screen.CallScreenActivity
import com.example.callcenter.utils.Active_Call_Notification_Id
import com.example.callcenter.utils.AppNotificationManager
import com.example.callcenter.utils.Incoming_Answer_Action
import com.example.callcenter.utils.Incoming_Reject_Action
import com.example.callcenter.utils.cancelNotification
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class CallNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var callHandler: CallHandler
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            when (intent?.action) {
                Incoming_Answer_Action -> {
                    val activityIntent = Intent(context, CallScreenActivity::class.java)
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(activityIntent)
                    callHandler.answer()
                }

                Incoming_Reject_Action -> {
                    callHandler.reject()
                }
            }
        }
    }
}

data class CallInfo(
    val number: String?,
    var name: String?,
    var note: String?,
    var contactName: String? = null
) {
    fun displayName(): String = contactName ?: name ?: number ?: "unknown"
}


@Singleton
class CallHandler @Inject constructor(
    val db: AppDb,
    val app: Application,
    private val repo: ContactRepository,
    private val notificationManager: AppNotificationManager
) {
    val state: BehaviorSubject<Int> = BehaviorSubject.create()
    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            state.onNext(newState)
            if (newState != Call.STATE_RINGING) {
                app.cancelNotification()
            }
            if (newState == Call.STATE_ACTIVE && incoming) {
                notificationManager.activeCall(callInfo!!)
            }
            if (newState == Call.STATE_DISCONNECTED) {
                app.cancelNotification(Active_Call_Notification_Id)
                callInfo = null
            }
        }
    }

    var incoming: Boolean = false
        private set

    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                value.registerCallback(callback)
                state.onNext(it.state)
                notify(value)
            }
            field = value
            if (value == null) {
                state.onNext(Call.STATE_DISCONNECTED)
            }
        }

    var callInfo: CallInfo? = null
        private set

    private fun notify(call: Call) {

        GlobalScope.launch(Dispatchers.IO) {
            callInfo = repo.getContact(call.details.handle.schemeSpecificPart)
            if (call.details.callDirection == DIRECTION_INCOMING) {
                incoming = true
                notificationManager.incoming(callInfo!!)
            } else {
                incoming = false
                notificationManager.outgoingCall(callInfo!!)
                val nin = Intent(Intent.ACTION_MAIN, null)
                nin.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                nin.data = call.details.handle
                nin.setClass(app, CallScreenActivity::class.java)
                app.applicationContext.startActivity(nin)
            }
        }
    }

    fun answer() {
        call?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnect() {
        call?.disconnect()
    }

    fun reject() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            call?.reject(Call.REJECT_REASON_DECLINED)
        } else {
            call?.reject(false, "")
        }
    }
}

//object OngoingCall {
//
//    val state: BehaviorSubject<Int> = BehaviorSubject.create()
//
//    private val callback = object : Call.Callback() {
//        override fun onStateChanged(call: Call, newState: Int) {
//            state.onNext(newState)
//        }
//    }
//
//    var call: Call? = null
//        set(value) {
//            field?.unregisterCallback(callback)
//            value?.let {
//                it.registerCallback(callback)
//                state.onNext(it.state)
//            }
//            field = value
//            if (value == null) {
//                state.onNext(Call.STATE_DISCONNECTED)
//            }
//        }
//
//    fun answer() {
//        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
//    }
//
//    fun disconnect() {
//        call!!.disconnect()
//
//    }
//}