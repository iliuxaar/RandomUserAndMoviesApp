package com.example.randomuserfeature.paging

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.repository.UsersRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PageListProvider @Inject constructor(private val usersRepository: UsersRepository) {

    private val pageSize = 15
    private lateinit var compositeDisposable: CompositeDisposable
    private val config: PagedList.Config
    private lateinit var usersDataSourceFactory: UsersDataSourceFactory

    private lateinit var pagedListApi: PagedList<User>
    private lateinit var pagedListDb: PagedList<User>

    val pagedListRelay = BehaviorRelay.create<PagedList<User>>()

    init {
        config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
    }

    private fun pagedListApiObservable() = RxPagedListBuilder<Long, User>(usersDataSourceFactory, config).buildObservable().subscribeOn(Schedulers.io())
    private fun pagedListDbObservable() = RxPagedListBuilder<Int, User>(usersRepository.getDbFactory(), config).buildObservable().take(1).subscribeOn(Schedulers.io())

    private fun getApiDataSource() = usersDataSourceFactory.usersDataSource

    private fun initPagedLists(){
        compositeDisposable.add( pagedListApiObservable().subscribe {
            pagedListApi = it
            pagedListRelay.accept(it)
        } )
        compositeDisposable.add( pagedListDbObservable().subscribe { pagedListDb = it } )
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable){
        this.compositeDisposable = compositeDisposable
        usersDataSourceFactory = UsersDataSourceFactory(usersRepository, compositeDisposable)
        initPagedLists()
    }

    fun changeDataSource(shouldUseDb: Boolean) {
        if (!::pagedListApi.isInitialized || !::pagedListDb.isInitialized) return
        if (shouldUseDb) pagedListRelay.accept(pagedListDb)
        else pagedListRelay.accept(pagedListApi)
    }

    fun refreshListApi() = pagedListApi.dataSource.invalidate()

    fun onRetryListApi() = (pagedListApi.dataSource as UsersDataSource).retry()

    fun apiLoadingStateObservable(): Observable<PagingLoadingState> = getApiDataSource().switchMap { it.loadingStateRelay }.subscribeOn(Schedulers.io())

    fun apiInitialStateObservable(): Observable<PagingLoadingState> = getApiDataSource().switchMap { it.initialStateRelay }.subscribeOn(Schedulers.io())

}