package com.example.callcenter

import android.app.Application
import android.content.Intent
import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.VideoProfile
import com.example.callcenter.entities.AppDb
import com.example.callcenter.screens.call_screen.CallScreenActivity
import com.example.callcenter.utils.AppNotificationManager
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CallHandler @Inject constructor(
    val db: AppDb,
    val app: Application,
    val notificationManager: AppNotificationManager
) {
    val state: BehaviorSubject<Int> = BehaviorSubject.create()
    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            state.onNext(newState)
        }
    }

    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                value.registerCallback(callback)
                state.onNext(it.state)
                if (value.details.callDirection == DIRECTION_INCOMING)
                    notificationManager.incoming(value)
                else {
                    outgoing(value)
                }
            }
            field = value
            if (value == null) {
                state.onNext(Call.STATE_DISCONNECTED)
            }
        }

    private fun outgoing(call: Call) {
        notificationManager.outgoingCall(call)
        val nin = Intent(Intent.ACTION_MAIN, null)
        nin.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        nin.data = call.details.handle
        nin.setClass(app, CallScreenActivity::class.java)
        app.applicationContext.startActivity(nin)
    }

    fun answer() {
        call?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnect() {
        call?.disconnect()
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