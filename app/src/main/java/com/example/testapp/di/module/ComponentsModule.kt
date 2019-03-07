package com.example.testapp.di.module

import com.example.randomuserfeature.di.BaseRandomUserDepsComponent
import com.example.randomuserfeature.di.DaggerMainRandomUserComponent
import com.example.randomuserfeature.di.MainRandomUserComponent
import com.example.testapp.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ComponentsModule {

    @Provides
    @Singleton
    fun provideRandomUserDeps(): BaseRandomUserDepsComponent {
        return object : BaseRandomUserDepsComponent {
            override fun provideRetrofit() = App.component.retrofit()
        }
    }

    @Provides
    @Singleton
    fun provideMainRandomUserComponent(randomUserDeps: BaseRandomUserDepsComponent): MainRandomUserComponent {
        return DaggerMainRandomUserComponent.builder()
            .baseRandomUserDepsComponent(randomUserDeps)
            .build()
    }

}