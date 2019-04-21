package com.example.randomuserfeature.interactor

import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.paging.PageListProvider
import com.example.randomuserfeature.repository.UsersRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UsersListInteractor @Inject constructor(
    private val usersRepository: UsersRepository,
    private val pagedListProvider: PageListProvider
) {

    private val compositeDisposable = CompositeDisposable()
    val shouldUseDataBaseRelay = BehaviorRelay.create<Boolean>()

    init {
        pagedListProvider.setCompositeDisposable(compositeDisposable)
        compositeDisposable.add(dataBaseRelayDisposable())
    }

    private fun dataBaseRelayDisposable() = shouldUseDataBaseRelay
        .subscribe {
            usersRepository.shouldUseDataBase(it)
            pagedListProvider.changeDataSource(it)
        }

    fun pagedListRelay() = pagedListProvider.pagedListRelay

    fun refreshListApi() { if (!shouldUseDataBaseRelay.value!!) pagedListProvider.refreshListApi() }

    fun onRetryListApi() { if (!shouldUseDataBaseRelay.value!!) pagedListProvider.onRetryListApi() }

    fun loadingStateObservable(): Observable<PagingLoadingState> = pagedListProvider.apiLoadingStateObservable()

    fun initialStateObservable(): Observable<PagingLoadingState> = pagedListProvider.apiInitialStateObservable()

    fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun saveUserToDb(user: User) = usersRepository.insertUserIntoTable(user)

}