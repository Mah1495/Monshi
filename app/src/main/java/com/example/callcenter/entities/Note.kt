package com.example.callcenter.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val number: String?,
    val note: String,
    val contactId: Int? = null
)

@Dao
interface NoteDao {
    @Query("select * from note where number=:number limit :limit")
    fun getAll(number: String, limit: Int): Flow<List<Note>>

    @Query("select * from note where contactId=:contactId Limit :limit")
    suspend fun getAll(contactId: Int, limit: Int): List<Note>

    @Upsert
    suspend fun addOrEdit(note: Note)

    @Delete
    suspend fun delete(note: Note)
}

