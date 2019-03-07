package com.example.testapp.di.module

import com.example.testapp.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @JvmField val STUB_URL = "https://stub.com"

    private val interceptor by lazy { HttpLoggingInterceptor() }
    init {
        interceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = interceptor

    @Provides
    @Singleton
    fun provideClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideGsonFactory() = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRxJavaFactory() = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, rxJavaFactory: RxJava2CallAdapterFactory, gsonFactory: GsonConverterFactory): Retrofit{
        return Retrofit.Builder()
                .baseUrl(STUB_URL)
                .addConverterFactory(gsonFactory)
                .addCallAdapterFactory(rxJavaFactory)
                .client(client)
                .build()
    }

}