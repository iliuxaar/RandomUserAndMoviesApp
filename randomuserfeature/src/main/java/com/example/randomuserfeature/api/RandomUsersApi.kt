package com.example.randomuserfeature.api

import com.example.randomuserfeature.api.entities.Users
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUsersApi {

    @GET("api/")
    fun getRandomUsers(@Query("results") size: Int, @Query("page") page: Int): Single<Users>

    @GET("api/")
    fun getUserById(@Query("id.value=") id: String): Single<Users>

}