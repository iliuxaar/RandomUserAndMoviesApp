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

    override fun onCreate() {
        super.onCreate()

        load()

        userClick.observable
            .subscribe(){
                sendMessage(ToastMessage())
            }
            .untilDestroy()
    }

    private fun load(){
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
            .subscribe (
                { users ->
                    Log.d("test", users.toString())
                    result.consumer.accept(users.results) },
                { error -> Log.d("errorTest", error.message)}
            )
    }

}

class ToastMessage() : NavigationMessage