package com.example.randomuserfeature.paging

import androidx.paging.DataSource
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.repository.UsersRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class UsersDataSourceFactory (
    private val usersRepository: UsersRepository,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Long, User>() {

    val usersDataSource = BehaviorSubject.create<UsersDataSource>()

    override fun create(): DataSource<Long, User> {
        val usersDataSource = UsersDataSource(usersRepository, compositeDisposable)
        this.usersDataSource.onNext(usersDataSource)
        return usersDataSource
    }
}