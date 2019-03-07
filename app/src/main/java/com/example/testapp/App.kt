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
        lateinit var instance: App
            private set
        val component by lazy {
            DaggerAppComponent.builder()
                .context(instance)
                .build()
        }

//        private val randomUserDeps: BaseRandomUserDepsComponent by lazy {
//            object : BaseRandomUserDepsComponent {
//                override fun provideRetrofit() = component.retrofit()
//            }
//        }
//
//        val mainRandomUserComponent: MainRandomUserComponent by lazy {
//            DaggerMainRandomUserComponent.builder()
//                .baseRandomUserDepsComponent(randomUserDeps)
//                .build()
//        }
    }
}