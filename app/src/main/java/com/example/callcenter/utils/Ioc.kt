package com.example.callcenter.utils

import android.content.Context
import androidx.room.Room
import com.example.callcenter.entities.AppDb

object Ioc {
    fun db(context: Context): AppDb {
        return Room.databaseBuilder(
            context,
            AppDb::class.java, "database-name"
        ).build()
    }
}