package com.example.randomuserfeature.di

import com.example.coremodule.navigation.Router
import retrofit2.Retrofit

interface BaseRandomUserDepsComponent{

    //write parents(app) randomUser dependencies here
    fun provideRetrofit(): Retrofit
    fun provideRouter(): Router

}