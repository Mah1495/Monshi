package com.example.callcenter

import android.telecom.Call
import android.telecom.VideoProfile

object OngoingCall {

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {

        }
    }

    public var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
            }
            field = value
        }

    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnect() {
        call!!.disconnect()

    }
}