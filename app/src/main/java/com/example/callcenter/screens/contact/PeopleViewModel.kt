package com.example.callcenter.screens.contact

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.Contact
import com.example.callcenter.screens.info.showInfo
import com.example.callcenter.screens.recent_call.CallsEvent
import com.example.callcenter.utils.call
import com.example.callcenter.utils.sms
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val db: AppDb,
    private val app: Application
) : ViewModel() {

    val contacts = db.contactDao().getAll()
    suspend fun add(contact: Contact) {
        db.contactDao().addOrEdit(contact)
    }

    fun onEvent(event: CallsEvent) {
        when (event) {
            is CallsEvent.Call -> {
                app.call(event.number)
            }

            is CallsEvent.Message -> {
                app.sms(event.number)
            }

            is CallsEvent.Info -> {
                app.showInfo(event.number)
            }

            is CallsEvent.Notes -> {

            }

            else -> {

            }
        }

    }
}