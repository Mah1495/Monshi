package com.example.callcenter.screens.contact

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.Contact
import com.example.callcenter.screens.call_screen.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AddEditPersonProperty {
    Name,
    Number
}

@HiltViewModel
class AddEditPersonViewModel @Inject constructor(
    private val db: AppDb
) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var number by mutableStateOf("")
        private set
    var image by mutableStateOf<Uri?>(null)
        private set

    private val _uiEvents = Channel<UiEvent>();
    val uiEvent = _uiEvents.receiveAsFlow()

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.Add -> {
                viewModelScope.launch {
                    db.contactDao()
                        .addOrUpdate(
                            Contact(
                                0,
                                name,
                                emptyList(),
                                emptyList(),
                                number,
                                image.toString()
                            )
                        )
                    _uiEvents.send(UiEvent.Done)
                }
            }

            is PersonEvent.ImageSelected -> {
                image = event.uri
            }

            is PersonEvent.OnPropertyChanged -> {
                when (event.property) {
                    AddEditPersonProperty.Name -> name = event.value
                    AddEditPersonProperty.Number -> number = event.value
                }
            }
        }
    }
}

sealed class PersonEvent {
    data class OnPropertyChanged(val property: AddEditPersonProperty, val value: String) :
        PersonEvent()

    data class ImageSelected(val uri: Uri?) : PersonEvent()
    object Add : PersonEvent()
}