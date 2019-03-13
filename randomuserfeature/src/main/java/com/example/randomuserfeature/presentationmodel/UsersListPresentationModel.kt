package com.example.randomuserfeature.presentationmodel

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.api.network.GitHubApi
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.paging.UsersDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class UsersListPresentationModel @Inject constructor(gitHubApi: GitHubApi) : ScreenPresentationModel() {

    val userItemClick = Action<User>()
    val refreshUsersAction = Action<Unit>()
    val retryActionClick = Action<Unit>()

    val isSwipeLoading = State(initialValue = false)
    val isInitialLoad = State<PagingLoadingState>()
    val isListLoading = State<PagingLoadingState>()

    private val pageSize = 15
    private val sourceFactory: UsersDataSourceFactory
    private val compositeDisposable = CompositeDisposable()
    lateinit var userList: LiveData<PagedList<User>>

    init {
        sourceFactory = UsersDataSourceFactory(compositeDisposable, gitHubApi)
        initPageList()
    }

    override fun onCreate() {
        super.onCreate()
        initActions()
        initPageList()
    }

    private fun initActions(){
        userItemClick.observable
            .subscribe { sendMessage(UserDetailsMessage(it)) }
            .untilDestroy()

        retryActionClick.observable
            .subscribe { sourceFactory.usersDataSource.value!!.retry() }
            .untilDestroy()

        refreshUsersAction.observable
            .subscribe{
                sourceFactory.usersDataSource.value!!.invalidate()
                isSwipeLoading.consumer.accept(false)
            }
            .untilDestroy()

        sourceFactory.usersDataSource
            .switchMap { it.loadingStateRelay }
            .subscribe { isListLoading.consumer.accept(it) }
            .untilDestroy()

        sourceFactory.usersDataSource
            .switchMap { it.initialStateRelay }
            .subscribe { isInitialLoad.consumer.accept(it)}
            .untilDestroy()

    }

    private fun initPageList(){
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        userList = LivePagedListBuilder<Long, User>(sourceFactory, config).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}