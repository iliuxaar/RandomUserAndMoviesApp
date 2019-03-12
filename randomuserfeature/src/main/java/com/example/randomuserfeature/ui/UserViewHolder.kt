package com.example.randomuserfeature.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuserfeature.R
import com.example.randomuserfeature.api.entities.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(view: View, private val itemClickListener: (User) -> Unit) : RecyclerView.ViewHolder(view) {

    private lateinit var user: User

    init {
        view.setOnClickListener {
            itemClickListener.invoke(user)
        }
    }

    fun bindTo(user: User) {
        this.user = user
        itemView.nameUser.text = user.login
        Glide.with(itemView)
            .load(user.avatarUrl)
            .thumbnail(Glide.with(itemView).load(R.drawable.loading))
            .circleCrop()
            .into(itemView.imageUser)
    }

    companion object {
        fun create(parent: ViewGroup, itemClickListener: (User) -> Unit): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, itemClickListener)
        }
    }

}