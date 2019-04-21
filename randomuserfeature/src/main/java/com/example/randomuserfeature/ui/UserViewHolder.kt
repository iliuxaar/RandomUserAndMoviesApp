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
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(
    view: View,
    private val itemClickListener: (User) -> Unit,
    private val saveClickListener: (User) -> Unit
) : RecyclerView.ViewHolder(view) {

    private lateinit var user: User
    private lateinit var task: Disposable


    init {
        view.setOnClickListener { itemClickListener.invoke(user) }
        itemView.dbCheckBox.setOnClickListener { saveClickListener.invoke(user) }
    }



    fun bindTo(user: User) {
        this.user = user
        task = user.isAddedToDb.subscribe{ if(it) swapAnimation(R.drawable.save_icon_animated_finish) }
        itemView.nameUser.text = user.login
        Glide.with(itemView)
            .load(user.avatarUrl)
            .thumbnail(Glide.with(itemView).load(R.drawable.loading))
            .circleCrop()
            .into(itemView.imageUser)
        itemView.dbCheckBox.setOnClickListener {
            itemView.dbCheckBox.isEnabled = false
            swapAnimation(R.drawable.save_icon_animated_start)
        }
    }

    private fun swapAnimation(@DrawableRes drawableResId: Int) {
        val avd = AnimatedVectorDrawableCompat.create(itemView.context, drawableResId)
        itemView.dbCheckBox.setImageDrawable(avd)
        (avd as Animatable).start()
    }

    fun dispose() {
        task.dispose()
    }

    companion object {
        fun create(parent: ViewGroup, itemClickListener: (User) -> Unit, saveClickListener: (User) -> Unit): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, itemClickListener, saveClickListener)
        }
    }

}