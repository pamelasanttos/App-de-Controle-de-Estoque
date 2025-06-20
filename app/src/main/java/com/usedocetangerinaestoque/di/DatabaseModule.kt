package com.usedocetangerinaestoque.di

import android.content.Context
import androidx.room.Room
import com.usedocetangerinaestoque.data.UseDoceTangerinaDatabase
import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.dao.ImageDao
import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UseDoceTangerinaDatabase {
        return Room.databaseBuilder(
            context,
            UseDoceTangerinaDatabase::class.java,
            "use-doce-tangerina-db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: UseDoceTangerinaDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    fun provideItemDao(database: UseDoceTangerinaDatabase): ItemDao {
        return database.getItemDao()
    }

    @Provides
    fun provideSizeDao(database: UseDoceTangerinaDatabase): SizeDao {
        return database.getSizeDao()
    }

    @Provides
    fun provideCategoryDao(database: UseDoceTangerinaDatabase): CategoryDao {
        return database.getCategoryDao()
    }

    @Provides
    fun provideImageDao(database: UseDoceTangerinaDatabase): ImageDao {
        return database.getImageDao()
    }
}
