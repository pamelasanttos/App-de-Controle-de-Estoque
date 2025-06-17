package com.usedocetangerinaestoque.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.usedocetangerinaestoque.data.entities.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    suspend fun getAll(): List<Image>

    @Insert
    suspend fun add(image: Image): Long

    @Insert
    suspend fun addMultiple(images: List<Image>)

    @Query("SELECT * FROM image WHERE itemId = :itemId")
    suspend fun getImagesByItem(itemId: Long): List<Image>

    @Query("DELETE FROM image WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: Long)
}