package com.example.randomuserfeature.di.module

import com.example.randomuserfeature.api.GitHubApi
import com.example.randomuserfeature.api.GitHubApi.Companion.GITHUB_API_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class RandomUsersModule {

    @Provides
    @Singleton
    fun provideCallAdapterFactory() = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideGitHubUserApi(retrofit: Retrofit, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) =
        retrofit.newBuilder()
            .baseUrl(GITHUB_API_URL)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
            .create(GitHubApi::class.java)
}