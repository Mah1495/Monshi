package com.example.callcenter

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import com.example.callcenter.databinding.ActivityCallIncomingBinding


fun Activity.cancelNotification(id: Int = 123) {
    this.getSystemService<NotificationManager>()?.cancel(id)
}

class CallIncomingActivity : Activity() {
    private lateinit var binding: ActivityCallIncomingBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTurnScreenOn(true)
        setShowWhenLocked(true)
        binding = ActivityCallIncomingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.who.text = intent.data?.schemeSpecificPart
        binding.answer.setOnClickListener {
            OngoingCall.answer()
            val myIntent = Intent(this, CallScreenActivity::class.java)
            startActivity(myIntent)
        }
        binding.reject.setOnClickListener {
            OngoingCall.call?.reject(Call.REJECT_REASON_DECLINED)
        }

        OngoingCall.state.subscribe {
            if (it != Call.STATE_RINGING) {
                this.cancelNotification()
                finish()
            }
        }
    }
}