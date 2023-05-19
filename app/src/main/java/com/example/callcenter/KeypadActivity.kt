package com.example.callcenter

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.core.view.allViews
import com.example.callcenter.databinding.KeypadBinding


class KeypadActivity : Activity() {

    private lateinit var binding: KeypadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KeypadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clear.visibility = View.GONE
        binding.root.allViews.forEach { view ->
            if (view is Button) {
                view.setOnTouchListener { v, m ->
                    if (v is Button) {

                        when (m.action) {
                            MotionEvent.ACTION_DOWN -> {
                                OngoingCall.call?.playDtmfTone(v.text.first())
                            }

                            MotionEvent.ACTION_UP -> {
                                OngoingCall.call?.stopDtmfTone()
                            }
                        }
                    }
                    return@setOnTouchListener true
                }
            }
        }
    }

}