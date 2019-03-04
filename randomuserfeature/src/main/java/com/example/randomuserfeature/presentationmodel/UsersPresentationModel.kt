package com.example.randomuserfeature.presentationmodel

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomuserfeature.ScreenPresentationModel
import com.example.randomuserfeature.api.RandomUsersApi
import com.example.randomuserfeature.api.entities.ResultsItem
import com.jakewharton.rxbinding3.recyclerview.RecyclerViewScrollEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dmdev.rxpm.navigation.NavigationMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class UsersPresentationModel: ScreenPresentationModel() {

    val userClick = Action<ResultsItem>()
    val result = State<List<ResultsItem>>()
    val isLoading = State(initialValue = false)
    val isError = State(initialValue = false)
    val retryClick = Action<Unit>()
    val swipeAction = Action<Unit>()
    val onScroll = Action<RecyclerViewScrollEvent>()

    override fun onCreate() {
        super.onCreate()

        load(true, 1)

        userClick.observable
            .subscribe {
                sendMessage(ToastMessage())
            }
            .untilDestroy()

        retryClick.observable
                .subscribe {
                    load(false, 1)
                }
                .untilDestroy()

        swipeAction.observable
            .subscribe{
                load(false, 1)
            }
            .untilDestroy()

        onScroll.observable
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
        load(false, page)
    }

    private fun load(error: Boolean, page: Int){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        val randomUserApi = retrofit.create(RandomUsersApi::class.java)

         var task = randomUserApi.getRandomUsers(10, page)
             .delay(2000, TimeUnit.MILLISECONDS)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnSubscribe {
                 if(error) throw Exception()
                 if (isError.value) isError.consumer.accept(false)
                 isLoading.consumer.accept(true)
             }
             .subscribe (
                 { users ->
                     Log.d("test", users.toString())
                     isLoading.consumer.accept(false)
                     result.consumer.accept(users.results)
                 },
                 { isError.consumer.accept(true)}
            )
    }

}

class ToastMessage() : NavigationMessage