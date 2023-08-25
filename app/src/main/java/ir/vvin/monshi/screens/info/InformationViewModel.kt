package ir.vvin.monshi.screens.info

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.vvin.monshi.entities.AppDb
import ir.vvin.monshi.entities.ContactRepository
import ir.vvin.monshi.entities.Note
import ir.vvin.monshi.screens.recent_call.CallsEvent
import ir.vvin.monshi.screens.recent_call.RecentCall
import ir.vvin.monshi.utils.call
import ir.vvin.monshi.utils.sms
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    val app: Application,
    val db: AppDb,
    private val repo: ContactRepository
) : ViewModel() {
    fun onEvent(event: CallsEvent) {
        when (event) {
            is CallsEvent.Call -> app.call(event.number)
            is CallsEvent.Message -> app.sms(event.number)
            else -> {}
        }
    }

    var number by mutableStateOf("")
}

@HiltViewModel
class NoteViewModel @Inject constructor(val db: AppDb) : ViewModel() {
    fun notes(number: String): Flow<List<Note>> {
        return db.noteDao().getAll(number, 100)
    }
}

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repo: ContactRepository) : ViewModel() {
    fun logs(number: String): List<RecentCall> {
        return repo.logs(number)
    }
}