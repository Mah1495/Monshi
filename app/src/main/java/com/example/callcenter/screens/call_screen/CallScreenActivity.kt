package com.example.callcenter.screens.call_screen

import android.os.Bundle
import android.telecom.Call
import android.telecom.InCallService
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.example.callcenter.CallHandler
import com.example.callcenter.screens.home.DialScreen
import com.example.callcenter.screens.ui.theme.CallCenterTheme
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
        supportActionBar?.hide()
        setContent {
            CallCenterTheme {
                Surface {
                    var sheetState =
                        rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
                    var scaffoldState =
                        rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
                    var scope = rememberCoroutineScope()
                    val keypadState = remember { mutableStateOf(false) }
                    LaunchedEffect(sheetState.isCollapsed) {
                        keypadState.value = false
                    }
                    BottomSheetScaffold(sheetContent = {
                        Surface {
                            DialScreen(standAlone = true, modifier = Modifier
                                .padding(1.dp)
                                .height(375.dp), closeSheet = {
                                scope.launch {
                                    sheetState.collapse()
                                    keypadState.value = false
                                }
                            })
                        }
                    }, sheetPeekHeight = 0.dp, scaffoldState = scaffoldState) {
                        Surface {
                            CallScreen(openSheet = {
                                scope.launch {
                                    sheetState.expand()
                                    keypadState.value = true
                                }
                            }, keypadState = keypadState)
                        }
                    }
                }
            }
        }
        callHandler.state.subscribe()
        {

            if (it == Call.STATE_DISCONNECTED) {
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

    }
}