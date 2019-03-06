package com.example.randomuserfeature.presentationmodel

import android.util.Log
import com.example.randomuserfeature.api.entities.ResultsItem
import com.example.randomuserfeature.di.RandomUserDeps
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserDetailsPresentationModel(
    private val userId: String
): com.example.coremodule.pm.ScreenPresentationModel() {

    val userResult = State<ResultsItem>()

    lateinit var loadTask: Disposable

    override fun onCreate() {
        super.onCreate()
        loadUser()
    }

    private fun loadUser(){
        loadTask = RandomUserDeps.randomUserApi.getUserById(userId)
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