package com.example.randomuserfeature.presentationmodel

import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.api.db.UsersDao
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.api.network.GitHubApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresentationModel @Inject constructor(
    private val userId: Long,
    val gitHubApi: GitHubApi,
    val usersDao: UsersDao
): ScreenPresentationModel() {

    val userResult = State<User>()
    val loadError = State(false)

    override fun onCreate() {
        super.onCreate()
        loadUser()
    }

    private fun loadUser(){
        loadError.consumer.accept(false)
        gitHubApi.getUserById(userId)
            .doOnNext { usersDao.insertUser(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ userResult.consumer.accept(it)}
            .untilDestroy()

    }
}