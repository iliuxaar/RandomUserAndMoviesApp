package com.example.testapp

import android.app.Application
import com.example.testapp.di.CoreComponent
import com.example.testapp.di.CoreDeps
import com.example.testapp.di.DaggerAppComponent
import com.example.testapp.di.DaggerCoreComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        component.inject(this)
    }

    companion object {
        lateinit var instance: App
            private set
        val component by lazy {
            DaggerAppComponent.builder()
                .context(instance)
                .build()
        }

        val coreComponent: CoreComponent by lazy {
            DaggerCoreComponent.builder()
                .coreDeps(object : CoreDeps {
                    override fun context() = instance
                })
                .build()
        }
    }
}