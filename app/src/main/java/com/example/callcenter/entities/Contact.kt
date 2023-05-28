package com.example.callcenter.entities

import android.graphics.ColorSpace.Named
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity
data class Contact(
    @PrimaryKey val id: Int, val name: String, val notes: List<String>, val numbers: List<String>
)

@Dao
interface ContactDao {

    @Query("select * from contact")
    suspend fun getAll(): List<Contact>

    @Upsert
    suspend fun addOrUpdate(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)
}