package com.example.randomuserfeature.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.randomuserfeature.api.entities.User

@Database(entities = [User::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class UsersDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersDao
}