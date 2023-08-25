package ir.vvin.monshi.screens.incoming_call

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import ir.vvin.monshi.CallHandler
import ir.vvin.monshi.screens.call_screen.CallScreenActivity
import ir.vvin.monshi.screens.ui.theme.MonshiTheme
import ir.vvin.monshi.utils.cancelNotification
import javax.inject.Inject


@AndroidEntryPoint
class IncomingCallActivity : AppCompatActivity() {
    @Inject
    lateinit var callHandler: CallHandler

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTurnScreenOn(true)
        setShowWhenLocked(true)
        supportActionBar?.hide()
        setContent {
            MonshiTheme {
                Surface {
                    IncomingCallScreen(accept = {
                        callHandler.answer()
                        val myIntent = Intent(this, CallScreenActivity::class.java)
                        startActivity(myIntent)
                    }, reject = {
                        callHandler.call?.reject(Call.REJECT_REASON_DECLINED)
                    })
                }
            }
        }

        callHandler.state.subscribe {
            if (it != Call.STATE_RINGING) {
                this.cancelNotification()
                finish()
            }
        }
    }
}
