package com.example.callcenter

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.callcenter.databinding.ActivityAppBinding

const val IncomingChannel = "incoming";
const val OutGoingChannel = "outgoing"

class AppActivity : AppCompatActivity() {

    private val roleManager: RoleManager by lazy {
        getSystemService(Context.ROLE_SERVICE) as RoleManager

    }
    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OngoingCall.state.subscribe {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            if (it != Call.STATE_RINGING) {
                this.cancelNotification()
            }
        }
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_app)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        requestRole()
        createChannel(IncomingChannel)
        createChannel(OutGoingChannel, NotificationManager.IMPORTANCE_LOW)
    }

    fun requestRole() {
//        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER))
//            return
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }
        launcher.launch(intent)
    }


    @SuppressLint("WrongConstant")
    fun createChannel(id: String, importance: Int = NotificationManager.IMPORTANCE_MAX) {
        val channel = NotificationChannel(
            id, "Calls", importance
        )
        // other channel setup stuff goes here.

        // We'll use the default system ringtone for our incoming call notification channel.  You can
        // use your own audio resource here.
        // other channel setup stuff goes here.

        // We'll use the default system ringtone for our incoming call notification channel.  You can
        // use your own audio resource here.
        if (id == IncomingChannel) {
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            channel.setSound(
                ringtoneUri,
                AudioAttributes.Builder() // Setting the AudioAttributes is important as it identifies the purpose of your
                    // notification sound.
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }

        val mgr = getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(channel)
    }
}