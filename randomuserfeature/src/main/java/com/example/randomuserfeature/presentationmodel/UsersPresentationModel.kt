package com.example.randomuserfeature.presentationmodel

import android.util.Log
import com.example.randomuserfeature.ScreenPresentationModel
import com.example.randomuserfeature.api.RandomUsersApi
import com.example.randomuserfeature.api.entities.ResultsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dmdev.rxpm.navigation.NavigationMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UsersPresentationModel: ScreenPresentationModel() {

    val userClick = Action<ResultsItem>()
    val result = State<List<ResultsItem>>()
    val isLoading = State(initialValue = false)
    val isError = State(initialValue = false)
    val retryClick = Action<Unit>()

    override fun onCreate() {
        super.onCreate()

        load(true)

        userClick.observable
            .subscribe {
                sendMessage(ToastMessage())
            }
            .untilDestroy()

        retryClick.observable
                .subscribe {
                    load(false)
                }
                .untilDestroy()
    }

    private fun load(error: Boolean){
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

        randomUserApi.getRandomUsers(10)
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
                    result.consumer.accept(users.results) },
                { error ->
                    isError.consumer.accept(true)}
            )
    }

}

class ToastMessage() : NavigationMessage