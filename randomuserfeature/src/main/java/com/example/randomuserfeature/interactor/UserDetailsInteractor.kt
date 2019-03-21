package com.example.randomuserfeature.interactor

import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.repository.UsersRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsInteractor @Inject constructor(
    private val repository: UsersRepository,
    private val userId: Long
) {

    fun getUser(): Observable<User> = repository.getUserById(userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}