package com.example.randomuserfeature.presentationmodel

import androidx.paging.PagedList
import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.interactor.UsersListInteractor
import javax.inject.Inject


class UsersListPresentationModel @Inject constructor(private val usersListInteractor: UsersListInteractor) : ScreenPresentationModel() {

    val userItemClick = Action<User>()
    val refreshUsersAction = Action<Unit>()
    val retryActionClick = Action<Unit>()
    val saveActionClick = Action<User>()
    val shouldUseDbClick = Action<Boolean>()

    val isPagedListReady = Command<PagedList<User>>()
    val isSwipeLoading = State(initialValue = false)
    val isInitialLoad = State<PagingLoadingState>()
    val isListLoading = State<PagingLoadingState>()
    val isUserInsertedToDb = State<Long>()

    override fun onCreate() {
        super.onCreate()
        initActions()
    }

    private fun initActions(){
        usersListInteractor.pagedListRelay()
            .subscribe{ isPagedListReady.consumer.accept(it)}
            .untilDestroy()

        shouldUseDbClick.observable
            .subscribe { usersListInteractor.shouldUseDataBaseRelay.accept(it) }
            .untilDestroy()

        userItemClick.observable
            .subscribe { sendMessage(UserDetailsMessage(it)) }
            .untilDestroy()

        retryActionClick.observable
            .subscribe { usersListInteractor.onRetryListApi() }
            .untilDestroy()

        saveActionClick.observable
            .switchMap { usersListInteractor.saveUserToDb(it).toObservable() }
            .subscribe { isUserInsertedToDb.consumer.accept(it) }
            .untilDestroy()

        refreshUsersAction.observable
            .subscribe{
                usersListInteractor.refreshListApi()
                isSwipeLoading.consumer.accept(false)
            }
            .untilDestroy()

        usersListInteractor.initialStateObservable()
            .subscribe { isInitialLoad.consumer.accept(it)}
            .untilDestroy()

        usersListInteractor.loadingStateObservable()
            .subscribe { isListLoading.consumer.accept(it) }
            .untilDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        usersListInteractor.onDestroy()
    }
}