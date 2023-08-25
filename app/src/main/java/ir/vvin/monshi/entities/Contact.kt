package ir.vvin.monshi.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import ir.vvin.monshi.utils.IGroupable
import kotlinx.coroutines.flow.Flow

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int,
    override val name: String,
    override val number: String,
    val imageUri: String?
) : IGroupable {
    override fun compare(item: IGroupable): Boolean {
        return name[0].uppercase() == item.name[0].uppercase()
    }

    override fun groupName(): String {
        return name[0].uppercase()
    }
}

@Dao
interface ContactDao {

    @Query("select * from contact")
    fun getAll(): Flow<List<Contact>>

    @Query(
        """select c.id,c.name,p.normalized as number from PhoneNumber p
    join contact c on p.contactId=c.id """
    )
    suspend fun all(): List<Contact>

    @Query("select * from Contact where id=:id")
    suspend fun get(id: Int): Contact

    @Upsert
    suspend fun addOrEdit(contact: Contact): Long

    @Delete
    suspend fun delete(contact: Contact)
}