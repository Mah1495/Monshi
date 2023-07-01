package com.example.callcenter.screens.info

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.callcenter.screens.ui.theme.CallCenterTheme
import dagger.hilt.android.AndroidEntryPoint

fun Application.showInfo(number: String) {
    val intent = Intent(this.applicationContext, InformationActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.putExtra("number", number)
    this.startActivity(intent)
}

@AndroidEntryPoint
class InformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallCenterTheme {
                informationScreen(intent.extras?.getString("number")!!)
            }
        }
    }
}