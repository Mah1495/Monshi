package com.example.callcenter.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class Note(@PrimaryKey(autoGenerate = true) val id: Int, val number: String, val note: String)

@Dao
interface NoteDao {
    @Query("select * from note where number=:number")
    fun getAll(number: String): Flow<List<Note>>

    @Upsert
    fun addOrEdit(note: Note)

    @Delete
    fun delete(note: Note)
}

