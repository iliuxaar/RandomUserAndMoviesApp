package com.example.testapp.di

import android.content.Context
import com.example.testapp.App
import com.example.testapp.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): AppComponent.Builder
        fun build(): AppComponent
    }

    fun retrofit(): Retrofit
}