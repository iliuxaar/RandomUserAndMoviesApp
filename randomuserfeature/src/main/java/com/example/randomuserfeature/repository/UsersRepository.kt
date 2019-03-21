package com.example.randomuserfeature.repository

import com.example.randomuserfeature.api.db.UsersDao
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.api.network.GitHubApi
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val usersDao: UsersDao
) {

    private var shouldUseDataBase = false

    fun shouldUseDataBase(should : Boolean){
        shouldUseDataBase = should
    }

    private fun getUsersFromApi(userId: Long, requestedLoadSize: Int): Single<List<User>> {
        return gitHubApi.getUsers(userId, requestedLoadSize)
    }

    private fun getUserByIdFromApi(userId: Long)= gitHubApi
        .getUserById(userId)

    private fun getUsersFromDb(userId: Long, requestedLoadSize: Int): Single<List<User>> {
        return usersDao.getUsers()
    }

    private fun getUserByIdFromDb(userId: Long) = usersDao.getUserById(userId)

    fun insertUserIntoTable(user: User) = usersDao.insertUser(user)

    fun getUsers(userId: Long, requestedLoadSize: Int): Single<List<User>> {
        if (shouldUseDataBase) return getUsersFromDb(userId, requestedLoadSize)
        else return getUsersFromApi(userId, requestedLoadSize)
    }

    fun getUserById(userId: Long): Observable<User> {
        if (shouldUseDataBase) return getUserByIdFromDb(userId)
        else return getUserByIdFromApi(userId)
    }

    fun getDbFactory() = usersDao.getUsersFactory()

}