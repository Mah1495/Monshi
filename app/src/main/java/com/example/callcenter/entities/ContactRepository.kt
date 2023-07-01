package com.example.callcenter.entities

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
import androidx.core.database.getStringOrNull
import com.example.callcenter.CallInfo
import com.example.callcenter.screens.recent_call.CallType
import com.example.callcenter.screens.recent_call.RecentCall
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

fun ContentResolver.findContact(number: String): String? {

    val uri = Uri.withAppendedPath(
        Contacts.CONTENT_FILTER_URI, Uri.encode(number)
    )
    val cursor = this.query(
        uri, arrayOf(Contacts.DISPLAY_NAME), null, null, null
    )

    cursor?.moveToNext()?.let {
        if (cursor.count > 0) {
            return cursor.getString(0)
        }
    }
    return null;
}

fun Application.findContact(number: String): String? {
    val norm = number.normalizePhone()
    return this.contentResolver.findContact(number) ?: this.contentResolver.findContact(norm)
    ?: this.contentResolver.findContact(norm.substring(2))
    ?: this.contentResolver.findContact("0${norm.substring(2)}")
}


@SuppressLint("Range")
fun Application.getContacts(search: String): MutableList<CallInfo> {
    val cursor = this.contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        arrayOf(
            ContactsContract.Data.DISPLAY_NAME_PRIMARY, ContactsContract.Data.DATA1
        ),//arrayOf(Contacts.DISPLAY_NAME, ContactsContract.Data.DATA1),
        "replace(data1,' ','') LIKE ? ",
        arrayOf("%$search%"),
        "${ContactsContract.Data.DISPLAY_NAME_PRIMARY} Limit 5"
    )
    val list = mutableListOf<CallInfo>()

    while (cursor != null && cursor.moveToNext() && cursor.count > 0) {
        list.add(CallInfo(cursor.getStringOrNull(1), cursor.getStringOrNull(0), null))
    }
    cursor?.close()
    return list
}

@SuppressLint("Range")
fun Application.getContacts(phoneUtils: PhoneNumberUtil): MutableMap<String, Contact> {
    val cursor = this.contentResolver.query(
        ContactsContract.Data.CONTENT_URI, arrayOf(
            ContactsContract.Data.DISPLAY_NAME_PRIMARY, ContactsContract.Data.DATA1
        ), "${ContactsContract.Data.HAS_PHONE_NUMBER}=1 ", null, null
    )
    val list = mutableMapOf<String, Contact>()

    while (cursor != null && cursor.moveToNext() && cursor.count > 0) {
        cursor.getStringOrNull(1)?.let { raw ->
            raw.internationalNumber(phoneUtils)?.let {
                list[it] = Contact(
                    0, cursor.getString(0), raw, null
                )
            }
        }
    }
    cursor?.close()
    return list
}

@SuppressLint("Range")
fun Application.searchLog(search: String, offset: Int): List<CallInfo> {
    val cursor = this.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        arrayOf(
            CallLog.Calls.NUMBER, CallLog.Calls.CACHED_LOOKUP_URI
        ),//arrayOf(Contacts.DISPLAY_NAME, ContactsContract.Data.DATA1),
        "${CallLog.Calls.NUMBER} LIKE ? and ${CallLog.Calls.CACHED_LOOKUP_URI} is null",
        arrayOf("%$search%"),
        "${CallLog.Calls.NUMBER} Limit $offset,15"
    )
    val list = mutableListOf<CallInfo>()

    while (cursor != null && cursor.moveToNext() && cursor.count > 0) {
        list.add(CallInfo(cursor.getStringOrNull(0), null, null, null))
    }
    cursor?.close()
    return list
}

private val projection = arrayOf(
    CallLog.Calls._ID,
    CallLog.Calls.NUMBER,
    CallLog.Calls.TYPE,
    CallLog.Calls.DATE,
    CallLog.Calls.DURATION,
    CallLog.Calls.CACHED_PHOTO_URI,
    CallLog.Calls.CACHED_NAME
)

@Singleton
class ContactRepository @Inject constructor(
    val db: AppDb, val app: Application, private val phoneUtils: PhoneNumberUtil
) {
    private var contacts: MutableMap<String, Contact> = mutableMapOf()

    init {
        GlobalScope.launch {
            val people = db.contactDao().all()
            contacts = app.getContacts(phoneUtils)
            people.forEach { item ->
                item.number.internationalNumber(phoneUtils)?.let {
                    if (!contacts.containsKey(it)) contacts[it] = item
                }
            }
        }
    }

    suspend fun getContact(number: String): CallInfo {
        val callInfo = CallInfo(number, null, null)
        callInfo.contactName = app.findContact(number)

        val contact = db.phoneNumberDao().getAll(callInfo.number).firstOrNull()?.contactId
        callInfo.name = db.contactDao().get(contact ?: 0)?.name
        if (contact != null) {
            callInfo.note = db.noteDao().getAll(contact, 1).firstOrNull()?.note
        } else {
            callInfo.note =
                db.noteDao().getAll(callInfo.number!!, 1).firstOrNull()?.firstOrNull()?.note
        }
        return callInfo
    }

    fun searchNumber(number: String): List<Contact> {
        var num = 0
        var list = mutableListOf<Contact>()
        for (contact in contacts) {
            if (contact.key.contains(number)) {
                list.add(contact.value)
                num++
            }
            if (num == 5)
                break
        }
        val map = mutableSetOf<String>()
        var offset = 0
        while (num < 5) {
            val hist = app.searchLog(number, offset)
            offset += 15
            if (hist.isEmpty()) {
                break
            }
            for (it in hist) {
                if (!contacts.containsKey(it.number?.internationalNumber(phoneUtils))) {
                    if (!map.contains(it.number)) {
                        list.add(Contact(0, it.number!!, it.number, null))
                        map.add(it.number)
                        num++
                    }
                }
                if (num == 5) {
                }
                break
            }
        }
        return list
    }

    fun logs(number: String? = null): List<RecentCall> {
        val selection = if (number == null) null else {
            "replace(trim(${CallLog.Calls.NUMBER},'+'),' ','') Like ?"
        }
        val cursor = app.contentResolver.query(
            CallLog.Calls.CONTENT_URI.buildUpon().build(),
            projection,
            selection,
            if (number == null) null else arrayOf(number),
            "date DESC"
        )
        val list: MutableList<RecentCall> = mutableListOf()
        while (cursor != null && cursor.moveToNext()) {
            val number = cursor.getStringOrNull(1) ?: "noname"

            val recentCall = RecentCall(
                contacts[number?.internationalNumber(phoneUtils) ?: ""]?.name ?: number,
                Date(cursor.getLong(3)).toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
                CallType.fromInt(cursor.getInt(2)).icon,
                number
            )
            list.add(recentCall)
        }
        return list
    }
}