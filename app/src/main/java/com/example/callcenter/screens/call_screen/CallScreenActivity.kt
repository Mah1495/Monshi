package com.example.callcenter.screens.call_screen

import android.os.Bundle
import android.telecom.Call
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.callcenter.CallHandler
import com.example.callcenter.screens.home.DialScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val Close_Call: String = "call.screen.close"

@AndroidEntryPoint
class CallScreenActivity : AppCompatActivity() {
    //    private lateinit var binding: CallScreenBinding
//    private var muted = false
//    private var speaker = false
//    private var hold = false
    @Inject
    lateinit var callHandler: CallHandler

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
            var scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
            var scope = rememberCoroutineScope()
            val keypadState = remember { mutableStateOf(false) }
            LaunchedEffect(sheetState.isCollapsed) {
                keypadState.value = false
            }
            BottomSheetScaffold(sheetContent = {
                DialScreen(standAlone = true, modifier = Modifier
                    .padding(1.dp)
                    .height(375.dp), closeSheet = {
                    scope.launch {
                        sheetState.collapse()
                        keypadState.value = false
                    }
                })
            }, sheetPeekHeight = 0.dp, scaffoldState = scaffoldState) {
                CallScreen(openSheet = {
                    scope.launch {
                        sheetState.expand()
                        keypadState.value = true
                    }
                }, keypadState = keypadState)
            }
        }
        callHandler.state.subscribe()
        {
            if (it == Call.STATE_DISCONNECTED) {
                finish()
            }
        }
//        val mIntentFilter = IntentFilter()
//        mIntentFilter.addAction("com.durga.action.close")
//
//        binding.disconnect.setOnClickListener {
//            OngoingCall.disconnect()
//            cancelNotification()
//            finish()
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            onBackInvokedDispatcher.unregisterOnBackInvokedCallback { }
//        }
//        binding.mute.setOnClickListener {
//            getSystemService<InCallService>()?.setMuted(muted)
//            muted = !muted
//        }
//        binding.speaker.setOnClickListener {
//            getSystemService<InCallService>()?.setAudioRoute(if (speaker) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_EARPIECE)
//            speaker = !speaker
//        }
//        binding.keypad.setOnClickListener {
//            var inte = Intent(this, KeypadActivity::class.java)
//            inte.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//            startActivity(inte)
//        }
//        binding.hold.setOnClickListener {
//            if (!hold)
//                OngoingCall.call?.hold()
//            else
//                OngoingCall.call?.unhold()
//            hold = !hold
//        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

    }
}