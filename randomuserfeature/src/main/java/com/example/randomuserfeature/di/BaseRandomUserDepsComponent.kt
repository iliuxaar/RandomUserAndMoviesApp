package com.example.randomuserfeature.di

import retrofit2.Retrofit

interface BaseRandomUserDepsComponent{

    //write parents(app) randomUser dependencies here
    fun provideRetrofit(): Retrofit

}