package ir.vvin.monshi.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Entity(tableName = "CallLog")
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
    @Query("select * from CallLog")
    suspend fun getAll(): List<CallLog>

    @Upsert
    suspend fun addOrUpdate(vararg log: CallLog)

    @Delete
    suspend fun delete(log: CallLog)
}

