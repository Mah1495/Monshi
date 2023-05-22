package com.example.callcenter

import android.telecom.Call
import android.telecom.VideoProfile
import io.reactivex.rxjava3.subjects.BehaviorSubject

object OngoingCall {

    val state: BehaviorSubject<Int> = BehaviorSubject.create()

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            state.onNext(newState)
        }
    }

    public var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                state.onNext(it.state)
            }
            field = value
            if (value == null) {
                state.onNext(Call.STATE_DISCONNECTED)
            }
        }

    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnect() {
        call!!.disconnect()

    }
}