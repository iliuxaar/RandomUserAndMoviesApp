package com.example.testapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.randomuserfeature.api.db.UsersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): UsersDatabase = Room.databaseBuilder(context, UsersDatabase::class.java, "database").build()

}