package ir.vvin.monshi.screens.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.vvin.monshi.CallHandler
import ir.vvin.monshi.entities.Contact
import ir.vvin.monshi.entities.ContactRepository
import ir.vvin.monshi.screens.call_screen.UiEvent
import ir.vvin.monshi.utils.call
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialViewModel @Inject constructor(
    val app: Application, val callHandler: CallHandler, val repo: ContactRepository
) : ViewModel() {
    var phoneNumber by mutableStateOf(TextFieldValue())
        private set

    private var pressed = false
    var sheet by mutableStateOf(false)
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    var list by mutableStateOf<List<Contact>>(emptyList())
        private set

    fun onEvent(event: DialEvent) {
        when (event) {
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

            is DialEvent.TextChanged -> {
                phoneNumber = event.tv
            }

            is DialEvent.CallSearchClicked -> {
                phoneNumber = phoneNumber.copy(text = event.call.number!!)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun call() {
        app.call(phoneNumber.text)
        phoneNumber = phoneNumber.copy(text = "")
        list = emptyList()
    }

    private fun numberClicked(value: String) {

        val insertionPosition = phoneNumber.selection.start
        val currentValue = phoneNumber.text

        val newTextValue = TextFieldValue(
            text = currentValue.substring(
                0, insertionPosition
            ) + value + currentValue.substring(insertionPosition),
            selection = TextRange(insertionPosition + value.length)
        )

        phoneNumber = newTextValue
        search()
    }

    private fun search() {
        if (phoneNumber.text.length > 2) {
            list = repo.searchNumber(phoneNumber.text)
        } else {
            list = emptyList()
        }
        Log.d("aaaaaaaaaaaaw", "-------------------------------------dsd" + list.size)
    }

    private fun numUp(number: String) {
        pressed = false
        if (sheet) callHandler.call?.stopDtmfTone()
        else numberClicked(number)
    }

    private fun numPressed(number: String) {
        if (sheet && !pressed) {
            callHandler.call?.playDtmfTone(number[0])
            phoneNumber = phoneNumber.copy(text = phoneNumber.text + number)
            pressed = true
        }
    }

    private fun backSpaced() {
        val deletePosition = phoneNumber.selection.start
        val currentValue = phoneNumber.text
        if (deletePosition < 1) return
        val newTextValue = TextFieldValue(
            text = currentValue.substring(0, deletePosition - 1) + currentValue.substring(
                deletePosition
            ), selection = TextRange(deletePosition - 1)
        )
        phoneNumber = newTextValue
        search()
    }

    private fun clear() {
        phoneNumber = phoneNumber.copy(text = "")
        list = emptyList()
    }
}

sealed class DialEvent {
    data class TextChanged(val tv: TextFieldValue) : DialEvent()
    data class NumberDown(val number: String) : DialEvent()
    data class NumberUp(val number: String) : DialEvent()
    data class CallSearchClicked(val call: Contact) : DialEvent()
    object Cleared : DialEvent()
    object Removed : DialEvent()
    object Called : DialEvent()
    object ClosePad : DialEvent()
}