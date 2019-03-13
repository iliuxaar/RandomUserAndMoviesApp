package com.example.randomuserfeature.api.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class User(
    @PrimaryKey val id: Long = 0,
    val login: String,      // username
    val type: String,       // ex. User
    val name: String,
    val company: String?,
    val location: String?,
    val email: String?,
    val bio: String?,        // add info
    @SerializedName("avatar_url")        val avatarUrl: String,
    @SerializedName("html_url")          val url: String,                // real url
    @SerializedName("following_url")     val followingUrl: String,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @SerializedName("repos_url")         val reposUrl: String,
    @SerializedName("blog")              val blogUrl: String?,
    @SerializedName("site_admin")        val siteAdmin: Boolean = false,
    @SerializedName("public_repos")      val publicRepositoriesCount: Int = 0,
    @SerializedName("followers")         val followersCount: Int = 0,
    @SerializedName("following")         val followingCount: Int = 0,
    @SerializedName("created_at")        val createdDate: Date?,
    @SerializedName("updated_at")        val updatedDate: Date?
    )