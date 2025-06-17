package com.usedocetangerinaestoque.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.usedocetangerinaestoque.data.entities.Item
import com.usedocetangerinaestoque.data.relations.ItemFull
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Transaction
    @Query("SELECT * FROM item")
    suspend fun getAll(): List<ItemFull>

    @Transaction
    @Query("SELECT * FROM item where id = :id")
    suspend fun getById(id: Long): ItemFull?

    @Insert
    suspend fun add(item: Item): Long

    @Query("SELECT EXISTS(SELECT name FROM item WHERE name = :name)")
    suspend fun existName(name: String): Boolean

    @Query("SELECT EXISTS(SELECT name FROM item WHERE name = :name AND id != :excludeId)")
    suspend fun existOtherName(name: String, excludeId: Long): Boolean

    @Transaction
    @Query("SELECT * FROM item")
    fun observeAll(): Flow<List<ItemFull>>

    @Delete
    suspend fun deleteById(item: Item)

    @Update
    suspend fun update(item: Item)
}