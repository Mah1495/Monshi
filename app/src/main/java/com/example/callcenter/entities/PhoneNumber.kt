package com.example.callcenter.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class PhoneNumber(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val contactId: Int,
    val number: String
)

@Dao
interface PhoneNumberDao {
    @Query("select * from PhoneNumber where contactId=:contactId")
    fun getAll(contactId: Int): Flow<List<PhoneNumber>>

    @Upsert
    fun addOrEdit(phoneNumber: PhoneNumber)

    @Delete
    fun delete(phoneNumber: PhoneNumber)
}