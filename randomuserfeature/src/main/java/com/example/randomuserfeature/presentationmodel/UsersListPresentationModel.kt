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
    val shouldUseDbClick = Action<Boolean>()

    val isPagedListReady = Command<PagedList<User>>()
    val isSwipeLoading = State(initialValue = false)
    val isInitialLoad = State<PagingLoadingState>()
    val isListLoading = State<PagingLoadingState>()

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