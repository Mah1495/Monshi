package com.example.callcenter.screens.home

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callcenter.CallHandler
import com.example.callcenter.screens.call_screen.UiEvent
import com.example.callcenter.utils.call
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialViewModel @Inject constructor(
    val context: Application,
    val callHandler: CallHandler
) : ViewModel() {
    val phoneNumber = mutableStateOf(TextFieldValue())

    private var pressed = false
    val sheet = mutableStateOf(false)
    private val _uiEvent = Channel<UiEvent>();
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DialEvent) {
        when (event) {
//            is DialEvent.NumberClick -> numberClicked(event.number)
            is DialEvent.NumberDown -> numPressed(event.number)
            is DialEvent.NumberUp -> numUp(event.number)
            is DialEvent.Called -> call()
            is DialEvent.Cleared -> clear()
            is DialEvent.Removed -> backSpaced()
            is DialEvent.ClosePad -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.CloseSheet)
                }
            }

            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    fun call() {
        context.call(phoneNumber.value.text)
        phoneNumber.value = phoneNumber.value.copy(text = "")
    }

    private fun numberClicked(value: String) {

        val insertionPosition = phoneNumber.value.selection.start
        val currentValue = phoneNumber.value.text

        val newTextValue = TextFieldValue(
            text = currentValue.substring(
                0,
                insertionPosition
            ) + value + currentValue.substring(insertionPosition),
            selection = TextRange(insertionPosition + value.length)
        )

        phoneNumber.value = newTextValue
    }

    private fun numUp(number: String) {
        pressed = false
        if (sheet.value)
            callHandler.call?.stopDtmfTone()
        else
            numberClicked(number)
    }

    private fun numPressed(number: String) {
        if (sheet.value && !pressed) {
            callHandler.call?.playDtmfTone(number[0])
            phoneNumber.value = phoneNumber.value.copy(text = phoneNumber.value.text + number)
            pressed = true
        }
    }

    private fun backSpaced() {
        val deletePosition = phoneNumber.value.selection.start
        val currentValue = phoneNumber.value.text
        if (deletePosition < 1)
            return
        val newTextValue = TextFieldValue(
            text = currentValue.substring(0, deletePosition - 1) + currentValue.substring(
                deletePosition
            ),
            selection = TextRange(deletePosition - 1)
        )
        phoneNumber.value = newTextValue
    }

    fun clear() {
        phoneNumber.value = phoneNumber.value.copy(text = "")
    }
}

sealed class DialEvent {
    data class NumberClick(val number: String) : DialEvent()
    data class NumberDown(val number: String) : DialEvent()
    data class NumberUp(val number: String) : DialEvent()
    object Cleared : DialEvent()
    object Removed : DialEvent()
    object Called : DialEvent()
    object ClosePad : DialEvent()
}