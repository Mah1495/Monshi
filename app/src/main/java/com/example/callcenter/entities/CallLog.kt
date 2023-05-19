package com.example.callcenter.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Entity(tableName = "CallLogs")
data class CallLog(
    @PrimaryKey
    val id: Int,
    val name: String,
    val number: String,
    val date: Date,
    val note: String
)

@Dao
interface CallLogDao {
    @Query("select * from CallLogs")
    fun getAll(): List<CallLog>

    @Upsert
    fun addOrUpdate(vararg log: CallLog)

    @Delete
    fun delete(log: CallLog)
}

