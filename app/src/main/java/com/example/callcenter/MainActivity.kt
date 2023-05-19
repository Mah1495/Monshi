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
import android.provider.CallLog
import android.provider.CallLog.Calls.LIMIT_PARAM_KEY
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.callcenter.databinding.MainBinding


const val IncomingChannel = "incoming";
const val OutGoingChannel = "outgoing"

class MainActivity : ComponentActivity() {
    private lateinit var binding: MainBinding
    private val REQUEST_ID = 1
    private val roleManager: RoleManager by lazy {
        getSystemService(Context.ROLE_SERVICE) as RoleManager

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRole()
        createChannel(IncomingChannel)
        createChannel(OutGoingChannel)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Call.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL, null)
            startActivity(i)
        }
        binding.logs.setOnClickListener {
            val calls = contentResolver.query(
                CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(LIMIT_PARAM_KEY, "10")
                    .build(),
                null, null, null, null
            )
            while (calls?.moveToNext() == true) {
                Toast.makeText(this, calls.getString(1), Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun requestRole() {
        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER))
            return
        var intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }
        launcher.launch(intent)
    }


    @SuppressLint("WrongConstant")
    fun createChannel(id: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
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
//
//    fun requestRole() {
//        Intent intent = roleManager.createRequestRoleIntent (RoleManager.ROLE_DIALER);
//        ActivityCompat.startActivityForResult(self, REQUEST_ID);
//    }
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data ) {
//        if (requestCode == REQUEST_ID) {
//            if (resultCode == android.app.Activity.RESULT_OK) {
//                // Your app is now the default dialer app
//            } else {
//                // Your app is not the default dialer app
//            }
//        }
//    }
}
