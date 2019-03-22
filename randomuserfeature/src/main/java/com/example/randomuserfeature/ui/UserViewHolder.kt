package com.example.randomuserfeature.ui

import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.example.randomuserfeature.R
import com.example.randomuserfeature.api.entities.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(view: View, private val itemClickListener: (User) -> Unit) : RecyclerView.ViewHolder(view) {

    private lateinit var user: User

    private var isDownloading: Boolean = false
    private var downloadingStartTimeMillis: Long = 0

    private var isCompleteAnimationPending: Boolean = false

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
        itemView.dbCheckBox.setOnClickListener {
            if (isCompleteAnimationPending) return@setOnClickListener
            if (isDownloading) {
                val delayMillis = 2666 - (System.currentTimeMillis() - downloadingStartTimeMillis) % 2666
                itemView.dbCheckBox.postDelayed({
                    swapAnimation(R.drawable.save_icon_animated_finish)
                    isCompleteAnimationPending = false
                }, delayMillis)
                isCompleteAnimationPending = true
            } else {
                swapAnimation(R.drawable.save_icon_animated_start)
                downloadingStartTimeMillis = System.currentTimeMillis()
            }
            isDownloading = !isDownloading
        }
    }

    private fun swapAnimation(@DrawableRes drawableResId: Int) {
        val avd = AnimatedVectorDrawableCompat.create(itemView.context, drawableResId)
        itemView.dbCheckBox.setImageDrawable(avd)
        (avd as Animatable).start()
    }

    companion object {
        fun create(parent: ViewGroup, itemClickListener: (User) -> Unit): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, itemClickListener)
        }
    }

}