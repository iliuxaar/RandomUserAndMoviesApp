package com.example.randomuserfeature.presentationmodel

import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.api.GitHubApi
import com.example.randomuserfeature.api.entities.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresentationModel @Inject constructor(
    private val userId: Long,
    val gitHubApi: GitHubApi
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { user -> userResult.consumer.accept(user) },
                { loadError.consumer.accept(true)}
            ).untilDestroy()
    }
}