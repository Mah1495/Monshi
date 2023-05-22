package com.example.callcenter

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import androidx.core.content.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.callcenter.databinding.CallScreenBinding

const val Close_Call: String = "call.screen.close"

class CallScreenActivity : Activity() {
    private lateinit var binding: CallScreenBinding
    private var muted = false
    private var speaker = false
    private var hold = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CallScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        OngoingCall.state.subscribe {
            if (it == Call.STATE_DISCONNECTED) {
                finish()
            }
        }
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("com.durga.action.close")

        binding.disconnect.setOnClickListener {
            OngoingCall.disconnect()
            cancelNotification()
            finish()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.unregisterOnBackInvokedCallback { }
        }
        binding.mute.setOnClickListener {
            getSystemService<InCallService>()?.setMuted(muted)
            muted = !muted
        }
        binding.speaker.setOnClickListener {
            getSystemService<InCallService>()?.setAudioRoute(if (speaker) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_EARPIECE)
            speaker = !speaker
        }
        binding.keypad.setOnClickListener {
            var inte = Intent(this, KeypadActivity::class.java)
            inte.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(inte)
        }
        binding.hold.setOnClickListener {
            if (!hold)
                OngoingCall.call?.hold()
            else
                OngoingCall.call?.unhold()
            hold = !hold
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

    }
}