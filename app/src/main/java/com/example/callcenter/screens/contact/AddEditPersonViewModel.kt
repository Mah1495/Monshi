package com.example.callcenter.screens.contact

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.Contact
import com.example.callcenter.entities.Note
import com.example.callcenter.entities.PhoneNumber
import com.example.callcenter.entities.normalizePhone
import com.example.callcenter.screens.call_screen.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AddEditPersonProperty {
    Name,
    Number,
    Note
}

@HiltViewModel
class AddEditPersonViewModel @Inject constructor(
    private val db: AppDb
) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var number by mutableStateOf("")
        private set
    var note by mutableStateOf("")
        private set

    private val _uiEvents = Channel<UiEvent>()
    val uiEvent = _uiEvents.receiveAsFlow()

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.Add -> {
                viewModelScope.launch {
                    val id = (db.contactDao()
                        .addOrEdit(
                            Contact(
                                0,
                                name,
                                number,
                                null
                            )
                        )).toInt()
                    db.noteDao().addOrEdit(Note(0, null, note, id))
                    db.phoneNumberDao()
                        .addOrEdit(PhoneNumber(0, id, number, number.normalizePhone()))
                    _uiEvents.send(UiEvent.Done)
                }
            }

            is PersonEvent.OnPropertyChanged -> {
                when (event.property) {
                    AddEditPersonProperty.Name -> name = event.value
                    AddEditPersonProperty.Number -> number = event.value
                    AddEditPersonProperty.Note -> note = event.value
                }
            }
        }
    }
}

sealed class PersonEvent {
    data class OnPropertyChanged(val property: AddEditPersonProperty, val value: String) :
        PersonEvent()

    object Add : PersonEvent()
}