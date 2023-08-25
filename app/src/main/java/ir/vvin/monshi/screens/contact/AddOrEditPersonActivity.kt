package ir.vvin.monshi.screens.contact

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import ir.vvin.monshi.screens.ui.theme.MonshiTheme

@AndroidEntryPoint
class AddOrEditPersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            MonshiTheme {
                Surface {
                    AddEditPersonScreen(done = { finish() })
                }
            }
        }
    }
}