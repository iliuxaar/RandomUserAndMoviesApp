package com.example.randomuserfeature.fragment

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuserfeature.R
import com.example.randomuserfeature.api.entities.User
import com.example.randomuserfeature.data.LoadingStatus
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.ui.LoadingViewHolder
import com.example.randomuserfeature.ui.UserViewHolder

class UsersListAdapter(
    private val itemClickListener: (User) -> Unit,
    private val retryClickListener: () -> Unit,
    private val saveClickListener: (User) -> Unit
) : PagedListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback) {

    private var currentLoadingState: PagingLoadingState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder.create(parent, itemClickListener, saveClickListener)
            R.layout.item_network_state -> LoadingViewHolder.create(parent, retryClickListener)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as UserViewHolder).bindTo(getItem(position)!!)
            R.layout.item_network_state -> (holder as LoadingViewHolder).bindTo(currentLoadingState!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_user
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as UserViewHolder).dispose()
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setLoadingState(pagingLoadingState: PagingLoadingState) {
        if (currentList != null) {
            if (currentList!!.size != 0){
                val previousState = currentLoadingState
                val hadExtraRow = hasExtraRow()
                currentLoadingState = pagingLoadingState
                val hasExtraRow = hasExtraRow()
                if( hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) notifyItemRemoved(super.getItemCount())
                    else notifyItemInserted(super.getItemCount())
                } else if (hasExtraRow && previousState !== pagingLoadingState) notifyItemChanged(super.getItemCount())
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return currentLoadingState != null && currentLoadingState!!.loadingStatus != LoadingStatus.SUCCESS
    }

    public fun onAddUserToDb(userId: Long){
        val index = currentList?.indexOfFirst { it.id == userId }
        currentList?.get(index!!)?.isAddedToDb?.accept(true)
    }

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

}