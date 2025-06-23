package com.usedocetangerinaestoque.di

import android.content.Context
import android.se.omapi.Session
import com.usedocetangerinaestoque.BuildConfig
import com.usedocetangerinaestoque.services.PasswordEncripter
import com.usedocetangerinaestoque.services.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext app: Context): Context = app

    @Provides
    @Singleton
    fun provideSessionManager(context: Context): SessionManager = SessionManager(context)

    @Provides
    fun provideSecretKey(): String {
        return BuildConfig.SECRET_KEY
    }

    @Provides
    @Singleton
    fun providePasswordEncripter(secretKey: String): PasswordEncripter {
        return PasswordEncripter(secretKey)
    }
}
