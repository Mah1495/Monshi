package com.example.callcenter.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

fun <T> String.obj(): List<T> {
    val sType = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(this, sType)
}

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromJsonString(value: String): List<String> {
        return value.obj()
    }

    @TypeConverter
    fun toJsonString(value: List<String>): String {
        return Gson().toJson(value)
    }
}