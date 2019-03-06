package com.example.testapp

import android.app.Application
import com.example.randomuserfeature.di.DaggerRandomUserComponent
import com.example.randomuserfeature.di.RandomUserComponent
import com.example.randomuserfeature.di.RandomUserDependencies
import com.example.testapp.di.DaggerAppComponent

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

        val randomUserComponent: RandomUserComponent by lazy {
            DaggerRandomUserComponent.builder()
                .randomUserDependencies(object : RandomUserDependencies {
                })
                .build()
        }
    }
}