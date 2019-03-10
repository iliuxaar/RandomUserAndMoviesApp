package com.example.testapp

import android.app.Application
import com.example.testapp.di.DaggerAppComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        component.inject(this)
    }

    companion object {
        const val ERROR_ACTION = "${BuildConfig.APPLICATION_ID}.action.ERROR"
        lateinit var instance: App
            private set
        val component by lazy {
            DaggerAppComponent.builder()
                .context(instance)
                .build()
        }
    }
}