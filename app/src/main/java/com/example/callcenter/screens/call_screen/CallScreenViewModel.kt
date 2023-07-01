package com.example.callcenter.screens.call_screen

import android.app.Application
import android.telecom.CallAudioState
import android.telecom.InCallService
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callcenter.CallHandler
import com.example.callcenter.screens.call_screen.CallEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallScreenViewModel @Inject constructor(
    val app: Application,
    val callHandler: CallHandler
) : ViewModel() {
    private val _uiEvents = Channel<UiEvent>();
    val uiEvent = _uiEvents.receiveAsFlow()
    val muted = mutableStateOf(false)
    val keypad = mutableStateOf(false)
    val speaker =
        mutableStateOf(
            app.getSystemService<InCallService>()?.callAudioState?.route == CallAudioState.ROUTE_SPEAKER
        )

    fun onEvent(event: CallEvent) {
        when (event) {
            is Disconnect -> {
                callHandler.disconnect()
            }

            is Mute -> {
                app.getSystemService<InCallService>()?.setMuted(muted.value)
                muted.value = !muted.value
            }

            is Speaker -> {
                app.getSystemService<InCallService>()
                    ?.setAudioRoute(if (speaker.value) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_EARPIECE)
                speaker.value = !speaker.value
            }

            is Keypad -> {
                viewModelScope.launch {
                    _uiEvents.send(UiEvent.OpenSheet)
                    keypad.value = true
                }
            }

            is ClosePad -> {
                keypad.value = false
            }

            else -> {}

        }
    }

}

sealed class CallEvent {
    object Disconnect : CallEvent()
    object Mute : CallEvent()
    object Speaker : CallEvent()
    object Hold : CallEvent()
    object AddCall : CallEvent()
    object Keypad : CallEvent()
    object ClosePad : CallEvent()
}

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    object CloseSheet : UiEvent()
    object OpenSheet : UiEvent()
    object Done : UiEvent()
    data class StartActivity(val id: Int) : UiEvent()
}