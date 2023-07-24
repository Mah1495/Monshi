package com.example.callcenter


import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import android.telecom.InCallService
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
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
//    private lateinit var mService: InCallService
//    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService().  */
//    private val connection = object : ServiceConnection {
//
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance.
//            val binder = service as CallService.LocalBinder
//            mService = binder.getService()
//            mBound = true
//        }
//
//        override fun onServiceDisconnected(arg0: ComponentName) {
//            mBound = false
//        }
//    }

    @Inject
    lateinit var callHandler: CallHandler

    @Inject
    lateinit var repo: ContactRepository

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

    override fun onStart() {
        super.onStart()
//        Intent(this, CallService::class.java).also { intent ->
//            bindService(intent, connection, Context.BIND_AUTO_CREATE)
//        }
    }

    override fun onStop() {
        super.onStop()
//        unbindService(connection)
//        mBound = false
    }

    private fun requestRole() {
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
        launcher.launch(intent)
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun btn() {
    Surface() {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
        }
    }
}