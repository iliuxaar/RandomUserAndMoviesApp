package com.example.testapp.di

import android.content.Context
import com.example.testapp.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): AppComponent.Builder
        fun build(): AppComponent
    }
}