package com.example.callcenter

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import android.widget.Button
import com.example.callcenter.databinding.ActivityDialerBinding

class DialerActivity : Activity(), View.OnClickListener {
    private lateinit var binding: ActivityDialerBinding
    private lateinit var tm: TelecomManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.keypad.target.text = ""
        tm = getSystemService(TELECOM_SERVICE) as TelecomManager
        binding.keypad.one.setOnClickListener(this)
        binding.keypad.two.setOnClickListener(this)
        binding.keypad.three.setOnClickListener(this)
        binding.keypad.four.setOnClickListener(this)
        binding.keypad.five.setOnClickListener(this)
        binding.keypad.six.setOnClickListener(this)
        binding.keypad.seven.setOnClickListener(this)
        binding.keypad.eight.setOnClickListener(this)
        binding.keypad.nine.setOnClickListener(this)
        binding.keypad.zero.setOnClickListener(this)
        binding.call.setOnClickListener {
            val uri = Uri.fromParts("tel", binding.keypad.target.text.toString(), null)
            val extras = Bundle()
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
            tm.placeCall(uri, extras)
        }
    }

    override fun onClick(p0: View?) {
        binding.keypad.target.text = "${binding.keypad.target.text}${(p0 as? Button)?.text}"
    }
}