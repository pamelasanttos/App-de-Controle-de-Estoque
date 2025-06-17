package com.usedocetangerinaestoque.data

import com.usedocetangerinaestoque.util.DateConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.dao.ImageDao
import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.*

@Database(version = 1, exportSchema = false, entities= [
    User::class,
    Item::class,
    Category::class,
    Size::class,
    Image::class
])
@TypeConverters(DateConverter::class)
abstract class UseDoceTangerinaDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    abstract fun getItemDao(): ItemDao

    abstract fun getSizeDao(): SizeDao

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getImageDao(): ImageDao
}