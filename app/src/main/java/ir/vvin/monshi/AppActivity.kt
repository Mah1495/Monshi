package ir.vvin.monshi


import android.app.role.RoleManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import ir.vvin.monshi.entities.ContactRepository
import ir.vvin.monshi.screens.contact.AddOrEditPersonActivity
import ir.vvin.monshi.screens.info.InformationActivity
import ir.vvin.monshi.screens.ui.theme.MonshiTheme
import ir.vvin.monshi.utils.AppNotificationManager
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
            MonshiTheme {
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
        if (roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
            repo.init()
            return
        }
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode < 0) {
                    repo.init()
                }
            }
        launcher.launch(intent)
    }
}