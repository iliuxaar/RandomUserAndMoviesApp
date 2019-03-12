package com.example.randomuserfeature.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuserfeature.R
import com.example.randomuserfeature.data.LoadingStatus
import com.example.randomuserfeature.data.PagingLoadingState
import kotlinx.android.synthetic.main.item_network_state.view.*

class LoadingViewHolder(view: View, private val retryClickListener: () -> Unit) : RecyclerView.ViewHolder(view) {

    init {
        itemView.retryLoadingButton.setOnClickListener{
            retryClickListener.invoke()
        }
    }

    fun bindTo(loadingState: PagingLoadingState){
        itemView.errorMessageTextView.visibility = if (loadingState.errorMessage != null) View.VISIBLE else View.GONE
        if (loadingState.errorMessage != null) {
            itemView.errorMessageTextView.text = loadingState.errorMessage
        }

        itemView.retryLoadingButton.visibility = if (loadingState.loadingStatus == LoadingStatus.FAILED) View.VISIBLE else View.GONE
        itemView.loadingProgressBar.visibility = if (loadingState.loadingStatus == LoadingStatus.LOADING) View.VISIBLE else View.GONE

        if (loadingState.loadingStatus == LoadingStatus.LOADING_WITH_ERROR) {
            itemView.retryLoadingButton.visibility = View.VISIBLE
            itemView.loadingProgressBar.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryClickListener: () -> Unit): LoadingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return LoadingViewHolder(view, retryClickListener)
        }
    }

}