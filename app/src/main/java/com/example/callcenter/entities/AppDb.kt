package com.example.callcenter.entities

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [CallLog::class, Contact::class],
    version = 1,
    exportSchema = true
//    autoMigrations = [AutoMigration(1, 2, AppDb.MyAutoMigration::class)]
)
@TypeConverters(value = [Converters::class])
abstract class AppDb : RoomDatabase() {
//    @RenameTable(fromTableName = "CallLogs", toTableName = "CallLog")
//    class MyAutoMigration : AutoMigrationSpec

    abstract fun logDao(): CallLogDao
    abstract fun contactDao(): ContactDao
}