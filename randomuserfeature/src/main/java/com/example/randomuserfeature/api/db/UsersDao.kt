package com.example.randomuserfeature.api.db

import androidx.paging.DataSource
import androidx.room.*
import com.example.randomuserfeature.api.entities.User
import io.reactivex.Observable
import io.reactivex.Single



@Dao
interface UsersDao {

    @Query("SELECT * FROM user")
    fun getUsers(): Single<List<User>>

    @Query("SELECT * FROM user")
    abstract fun getUsersFactory(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Long): Observable<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

}