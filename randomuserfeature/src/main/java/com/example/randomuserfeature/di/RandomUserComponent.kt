package com.example.randomuserfeature.di

import com.example.randomuserfeature.api.RandomUsersApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//TODO: add dagger late
class RandomUserComponent {
    companion object {

        private val interceptor = HttpLoggingInterceptor()
        init {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        val randomUserApi = retrofit.create(RandomUsersApi::class.java)
    }
}