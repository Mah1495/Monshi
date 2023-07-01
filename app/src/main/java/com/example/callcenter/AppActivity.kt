package com.example.callcenter


import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import com.example.callcenter.entities.ContactRepository
import com.example.callcenter.screens.contact.AddOrEditPersonActivity
import com.example.callcenter.screens.info.InformationActivity
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
    lateinit var repo: ContactRepository

    @Inject
    lateinit var notificationManager: AppNotificationManager

    private val roleManager: RoleManager by lazy {
        getSystemService(ROLE_SERVICE) as RoleManager
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestRole()
        supportActionBar?.hide()
        setContent {
            CallCenterTheme {
                Surface {
                    MainScreenView {
                        when (it) {
                            0 -> startActivity(Intent(this, AddOrEditPersonActivity::class.java))
                            1 -> startActivity(Intent(this, InformationActivity::class.java))
                        }
                    }

                }
            }
        }
        notificationManager.createOutgoingChannel()
        notificationManager.createIncomingChannel()
    }


    private fun requestRole() {
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            }
        launcher.launch(intent)
    }
}
