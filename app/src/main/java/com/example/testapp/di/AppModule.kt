package com.example.testapp.di

import com.example.coremodule.navigation.RouterProvider
import dagger.Module
import dagger.Provides


@Module
object AppModule {

    @Provides
    @JvmStatic fun router() = RouterProvider.router

}