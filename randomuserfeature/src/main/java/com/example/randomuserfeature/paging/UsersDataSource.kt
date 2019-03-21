package com.example.randomuserfeature.paging

import androidx.paging.ItemKeyedDataSource
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.repository.UsersRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

internal const val RETRY_LIMIT = 5

class UsersDataSource(
    private val usersRepository: UsersRepository,
    private val compositeDisposable: CompositeDisposable
) : ItemKeyedDataSource<Long, User>() {

    val loadingStateRelay = BehaviorRelay.create<PagingLoadingState>()
    val initialStateRelay = BehaviorRelay.create<PagingLoadingState>()
    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<User>) {
        doBeforeLoad()
        initialStateRelay.accept(PagingLoadingState.LOADING)
        onLoad(usersRepository.getUsers(1, params.requestedLoadSize)
            .retryWhen(this::autoRetry)
            .subscribe(
                {
                    initialStateRelay.accept(PagingLoadingState.COMPLETE)
                    doOnSuccess(it, callback)
                },
                {
                    setRetry(Action { loadInitial(params, callback) })
                    initialStateRelay.accept(PagingLoadingState.ERROR(it.message))
                    doOnError(it)
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<User>) {
        doBeforeLoad()
        onLoad(usersRepository.getUsers(params.key, params.requestedLoadSize)
            .retryWhen(this::autoRetry)
            .subscribe(
                { doOnSuccess(it, callback) },
                {
                    doOnError(it)
                    setRetry(Action { loadAfter(params, callback) })
                }
            )
        )
    }

    private fun doBeforeLoad() {
        loadingStateRelay.accept(PagingLoadingState.LOADING)
    }

    private fun onLoad(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun doOnSuccess(users: List<User>, callback: LoadCallback<User>) {
        loadingStateRelay.accept(PagingLoadingState.COMPLETE)
        callback.onResult(users)
    }

    private fun doOnError(throwable: Throwable) {
        loadingStateRelay.accept(PagingLoadingState.ERROR(throwable.message))
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    private fun autoRetry(errors: Flowable<Throwable>): Flowable<Long> {
        return errors.zipWith(
            Flowable.range(1, RETRY_LIMIT + 1),
            BiFunction<Throwable, Int, Int>{ error: Throwable, retryCount: Int ->
                if (retryCount > RETRY_LIMIT) {
                    throw error
                } else {
                    initialStateRelay.accept(PagingLoadingState.LOADING_WITH_ERROR(error.message))
                    loadingStateRelay.accept(PagingLoadingState.LOADING_WITH_ERROR(error.message))
                    retryCount
                }
            }

        ).flatMap { retryCount: Int ->
            Flowable.timer(
                Math.pow(2.toDouble(), retryCount.toDouble()).toLong(),
                TimeUnit.SECONDS
            )
        }
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<User>) {

    }

    override fun getKey(item: User) = item.id

}