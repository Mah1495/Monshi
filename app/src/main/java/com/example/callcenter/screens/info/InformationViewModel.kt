package com.example.callcenter.screens.info

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.ContactRepository
import com.example.callcenter.entities.Note
import com.example.callcenter.entities.searchLog
import com.example.callcenter.screens.recent_call.CallsEvent
import com.example.callcenter.screens.recent_call.RecentCall
import com.example.callcenter.utils.call
import com.example.callcenter.utils.sms
import dagger.hilt.android.lifecycle.HiltViewModel
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