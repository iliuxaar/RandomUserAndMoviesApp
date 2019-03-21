package com.example.randomuserfeature.presentationmodel

import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.interactor.UserDetailsInteractor
import javax.inject.Inject

class UserDetailsPresentationModel @Inject constructor(
    private val userDetailsInteractor: UserDetailsInteractor
): ScreenPresentationModel() {

    val userResult = State<User>()
    val loadError = State(false)

    override fun onCreate() {
        super.onCreate()
        loadUser()
    }

    private fun loadUser(){
        loadError.consumer.accept(false)
        userDetailsInteractor.getUser()
            .subscribe{ userResult.consumer.accept(it)}
            .untilDestroy()

    }
}