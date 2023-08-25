package ir.vvin.monshi.screens.info

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import ir.vvin.monshi.screens.ui.theme.MonshiTheme

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
        supportActionBar?.hide()
        setContent {
            MonshiTheme {
                Surface {
                    informationScreen(intent.extras?.getString("number")!!)
                }
            }
        }
    }
}