package com.usedocetangerinaestoque.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usedocetangerinaestoque.data.entities.Size
import kotlinx.coroutines.flow.Flow

@Dao
interface SizeDao {
    @Query("SELECT * FROM size")
    fun observeAll(): Flow<List<Size>>

    @Query("SELECT * FROM size where id = :id")
    suspend fun getById(id: Long): Size?

    @Insert
    suspend fun add(size: Size)

    @Query("SELECT EXISTS(SELECT name FROM size WHERE name = :name)")
    suspend fun existName(name: String): Boolean

    @Delete
    suspend fun deleteById(size: Size)

    @Update
    suspend fun update(size: Size)
}