package com.example.callcenter.screens.contact

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import com.example.callcenter.screens.ui.theme.CallCenterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrEditPersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            CallCenterTheme {
                Surface {
                    AddEditPersonScreen(done = { finish() })
                }
            }
        }
    }
}