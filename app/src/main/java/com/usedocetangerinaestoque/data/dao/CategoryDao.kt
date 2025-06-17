package com.usedocetangerinaestoque.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usedocetangerinaestoque.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun observerAll(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getById(id: Long): Category?

    @Insert
    suspend fun add(category: Category): Long

    @Query("SELECT EXISTS(SELECT name FROM category WHERE name = :name)")
    suspend fun existName(name: String): Boolean

    @Delete
    suspend fun deleteById(category: Category)

    @Update
    suspend fun update(category: Category)
}