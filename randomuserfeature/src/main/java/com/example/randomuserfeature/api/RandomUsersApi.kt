package com.example.randomuserfeature.api

import com.example.randomuserfeature.api.entities.Users
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Hari on 20/11/17.
 */

interface RandomUsersApi {

    @GET("api/")
    fun getRandomUsers(@Query("results") size: Int, @Query("page") page: Int): Single<Users>
}