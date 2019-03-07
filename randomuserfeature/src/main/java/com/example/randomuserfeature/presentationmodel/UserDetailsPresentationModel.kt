package com.example.randomuserfeature.presentationmodel

import android.util.Log
import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.api.RandomUsersApi
import com.example.randomuserfeature.api.entities.ResultsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresentationModel @Inject constructor(
    private val userId: String,
    val randomUsersApi: RandomUsersApi
): ScreenPresentationModel() {

    val userResult = State<ResultsItem>()

    lateinit var loadTask: Disposable

    override fun onCreate() {
        super.onCreate()
        loadUser()
    }

    private fun loadUser(){
        loadTask = randomUsersApi.getUserById(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { users ->
                    userResult.consumer.accept(users.results!![0])
                },
                { Log.e("test", it.message)}
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        loadTask.dispose()
    }
}