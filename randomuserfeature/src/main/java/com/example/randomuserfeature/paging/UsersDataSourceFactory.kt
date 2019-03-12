package com.example.randomuserfeature.paging

import androidx.paging.DataSource
import com.example.randomuserfeature.api.GitHubApi
import com.example.randomuserfeature.api.entities.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class UsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val gitHubApi: GitHubApi
): DataSource.Factory<Long, User>() {

    val usersDataSource = BehaviorSubject.create<UsersDataSource>()

    override fun create(): DataSource<Long, User> {
        val usersDataSource = UsersDataSource(gitHubApi, compositeDisposable)
        this.usersDataSource.onNext(usersDataSource)
        return usersDataSource
    }
}