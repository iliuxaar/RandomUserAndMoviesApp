package com.example.randomuserfeature.di

import com.example.randomuserfeature.di.module.RandomUsersModule
import com.example.randomuserfeature.di.screencomponent.UserDetailsScreenComponent
import com.example.randomuserfeature.di.screencomponent.UsersListScreenComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RandomUsersModule::class], dependencies = [BaseRandomUserDepsComponent::class])
@Singleton
interface MainRandomUserComponent {

    fun plusUsersScreenComponent(): UsersListScreenComponent
    fun plusUserDetailsScreenComponent(): UserDetailsScreenComponent.Builder

}