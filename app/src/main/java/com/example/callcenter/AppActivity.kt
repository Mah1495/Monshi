package com.example.callcenter

import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.callcenter.screens.contact.AddOrEditPersonActivity
import com.example.callcenter.screens.ui.theme.CallCenterTheme
import com.example.callcenter.utils.AppNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val IncomingChannel = "incoming";
const val OutGoingChannel = "outgoing"

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    @Inject
    lateinit var callHandler: CallHandler

    @Inject
    lateinit var notificationManager: AppNotificationManager

    private val roleManager: RoleManager by lazy {
        getSystemService(ROLE_SERVICE) as RoleManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRole()
        supportActionBar?.hide()
        setContent {
            CallCenterTheme {
                MainScreenView {
                    startActivity(Intent(this, AddOrEditPersonActivity::class.java))
                }
            }
        }

        notificationManager.createOutgoingChannel()
        notificationManager.createIncomingChannel()
    }

    private fun requestRole() {
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        }
        launcher.launch(intent)
    }
}