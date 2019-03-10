package com.example.testapp.di.module

import com.example.testapp.BuildConfig
import com.example.testapp.factory.RetryCallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

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
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideGsonFactory() = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetryFactory(): RetryCallAdapterFactory = RetryCallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        rxJavaFactory: RetryCallAdapterFactory,
        gsonFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(STUB_URL)
        .addConverterFactory(gsonFactory)
        .addCallAdapterFactory(rxJavaFactory)
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideRetrySubject() = PublishSubject.create<Unit>()

    private companion object {
        private const val STUB_URL = "https://stub.com"
    }

}