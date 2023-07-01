package com.example.callcenter.entities

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.example.callcenter.CallInfo
import com.example.callcenter.utils.parse
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.flow.Flow

fun String.normalizePhone(): String {
    val norm = this.replace(" ", "").trimStart('+')
    return norm
    if (norm.startsWith("0")) return "98" + norm.substring(1)
    if (norm.startsWith("98")) return norm;
    return "98$norm"
}

fun String.internationalNumber(phoneUtils: PhoneNumberUtil): String? {
    return try {
        val parsed = phoneUtils.parse(this.replace(" ", ""))
        "+${parsed.countryCode}${parsed.nationalNumber}"
    } catch (ex: NumberParseException) {
        null
    }
}

fun String.isValidPhone(phoneUtils: PhoneNumberUtil): Boolean {
    return try {
        phoneUtils.isValidNumber(phoneUtils.parse(this))
    } catch (ex: NumberParseException) {
        false
    }
}

fun String.nationalNumber(phoneUtils: PhoneNumberUtil): String? {
    return try {
        val parsed = phoneUtils.parse(this.replace(" ", ""))
        parsed.nationalNumber.toString()
    } catch (ex: NumberParseException) {
        null
    }
}

@Entity
data class PhoneNumber(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val contactId: Int,
    val number: String,
    val normalized: String
)

@Dao
interface PhoneNumberDao {
    @Query("select * from PhoneNumber where contactId=:contactId")
    fun getAll(contactId: Int): Flow<List<PhoneNumber>>

    @Query("select * from PhoneNumber where number=:number Limit 1")
    suspend fun getAll(number: String?): List<PhoneNumber>

    @Query(
        """select p.number,c.name,null,null from PhoneNumber p
    join contact c on p.contactId=c.id 
    where p.normalized like '%'+:number+'%'
    Limit :limit"""
    )
    suspend fun getWithDetail(number: String, limit: Int): List<CallInfo>

    @Upsert
    suspend fun addOrEdit(phoneNumber: PhoneNumber)

    @Delete
    suspend fun delete(phoneNumber: PhoneNumber)
}