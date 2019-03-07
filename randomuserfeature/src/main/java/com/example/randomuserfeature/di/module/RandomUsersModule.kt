package com.example.randomuserfeature.di.module

import com.example.randomuserfeature.api.RandomUsersApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RandomUsersModule {

    @Provides
    @Singleton
    fun provideRandomUserApi(retrofit: Retrofit) =
        retrofit.newBuilder()
            .baseUrl(RandomUsersApi.API_URL)
            .build()
            .create(RandomUsersApi::class.java)
}