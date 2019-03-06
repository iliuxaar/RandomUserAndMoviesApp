package com.example.randomuserfeature.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuserfeature.R
import com.example.randomuserfeature.api.entities.ResultsItem
import com.example.randomuserfeature.utils.inflate
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter(
    private val itemClickListener: (result: ResultsItem) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private val users: MutableList<ResultsItem> = mutableListOf()

    fun addUsers(users: List<ResultsItem>) {
        val positionForInsert = itemCount
        this.users.addAll(users)
        notifyItemRangeInserted(positionForInsert, users.size)
    }

    fun clearItems(){
        this.users.clear()
    }

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_user))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var result: ResultsItem

        init {
            itemView.setOnClickListener {
                itemClickListener.invoke(result)
            }
        }

        fun bind(result: ResultsItem) {
            this.result = result
            itemView.nameUser.text = result.name.toString()
            Glide.with(itemView)
                .load(result.picture.medium)
                .circleCrop()
                .into(itemView.imageUser)
        }
    }
}