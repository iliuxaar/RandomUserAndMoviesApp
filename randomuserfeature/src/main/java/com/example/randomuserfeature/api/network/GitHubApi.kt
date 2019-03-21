package com.example.randomuserfeature.api.network

import com.example.randomuserfeature.api.entities.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/users")
    fun getUsers(@Query("since") userId: Long, @Query("per_page") perPage: Int): Single<List<User>>

    @GET("/user/{id}")
    fun getUserById(@Path("id") userId: Long): Observable<User>

    companion object {
        const val GITHUB_API_URL = "https://api.github.com/"
    }

}