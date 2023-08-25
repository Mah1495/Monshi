package ir.vvin.monshi.screens.contact

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.vvin.monshi.entities.AppDb
import ir.vvin.monshi.entities.Contact
import ir.vvin.monshi.screens.info.showInfo
import ir.vvin.monshi.screens.recent_call.CallsEvent
import ir.vvin.monshi.utils.call
import ir.vvin.monshi.utils.sms
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