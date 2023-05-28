package com.example.callcenter.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callcenter.entities.AppDb
import com.example.callcenter.entities.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val db: AppDb
) : ViewModel() {

    suspend fun contacts(): List<Contact> {
        return db.contactDao().getAll()
    }

    suspend fun add(contact: Contact) {
        db.contactDao().addOrUpdate(contact)
    }

}