package com.example.callcenter.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CallLog::class], version = 1)
@TypeConverters(value = [Converters::class])
abstract class AppDb : RoomDatabase() {
    abstract fun logDao(): CallLogDao
}