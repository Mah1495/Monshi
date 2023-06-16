package com.example.callcenter.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.example.callcenter.utils.IGroupable
import com.example.callcenter.utils.IImage
import kotlinx.coroutines.flow.Flow

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int,
    override val name: String,
    val notes: List<String>,
    val numbers: List<String>,
    override val number: String,
    override val imageUri: String?
) : IGroupable, IImage {
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

    @Upsert
    suspend fun addOrUpdate(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)
}