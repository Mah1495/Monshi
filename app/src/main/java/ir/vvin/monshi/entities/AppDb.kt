package ir.vvin.monshi.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CallLog::class, Contact::class, Note::class, PhoneNumber::class],
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
    abstract fun noteDao(): NoteDao
    abstract fun phoneNumberDao(): PhoneNumberDao
}