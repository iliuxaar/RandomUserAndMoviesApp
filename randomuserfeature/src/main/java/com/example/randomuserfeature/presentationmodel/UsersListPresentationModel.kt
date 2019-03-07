package com.example.randomuserfeature.presentationmodel

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coremodule.pm.ScreenPresentationModel
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.api.RandomUsersApi
import com.example.randomuserfeature.api.entities.ResultsItem
import com.jakewharton.rxbinding3.recyclerview.RecyclerViewScrollEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UsersListPresentationModel @Inject constructor(val randomUserApi: RandomUsersApi) : ScreenPresentationModel() {

    val userItemClick = Action<ResultsItem>()
    val retryButtonClick = Action<Unit>()
    val refreshUsersAction = Action<Unit>()
    val scrollListAction = Action<RecyclerViewScrollEvent>()

    val loadedResult = State<List<ResultsItem>>()
    val isLoading = State(initialValue = false)
    val isError = State(initialValue = false)

    lateinit var loadTask: Disposable

    override fun onCreate() {
        super.onCreate()
        initActions()
        load()
    }

    private fun initActions(){
        userItemClick.observable
            .subscribe { sendMessage(UserDetailsMessage(it)) }
            .untilDestroy()

        retryButtonClick.observable
            .subscribe { load() }
            .untilDestroy()

        refreshUsersAction.observable
            .subscribe{ load() }
            .untilDestroy()

        scrollListAction.observable
            .subscribe(){
                val layoutManager = (it.view.layoutManager as LinearLayoutManager)
                val totalItemCount = layoutManager.itemCount
                val updatePosition = totalItemCount - 10 / 2
                val firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading.value) {
                    if (firstVisibleItemPosition >= updatePosition && layoutManager.findFirstVisibleItemPosition() != 0) {
                        loadMoreItems((totalItemCount / 10) + 1)
                    }
                }
            }
            .untilDestroy()
    }

    private fun loadMoreItems(page: Int){
        load(page)
    }

    /**
     * Load only first page
     */
    private fun load(){
        load(1)
    }

    /**
     * Load page depending on the page
     * @param page - number of load page
     */
    private fun load(page: Int){
         loadTask = randomUserApi.getRandomUsers(10, page)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnSubscribe {
                 if (isError.value) isError.consumer.accept(false)
                 isLoading.consumer.accept(true)
             }
             .subscribe (
                 { users ->
                     isLoading.consumer.accept(false)
                     loadedResult.consumer.accept(users.results)
                 },
                 {
                     isError.consumer.accept(true)
                     Log.d("test", it.message)
                 }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        loadTask.dispose()
    }
}