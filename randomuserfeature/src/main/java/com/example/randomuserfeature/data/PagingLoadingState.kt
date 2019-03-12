package com.example.randomuserfeature.data

class PagingLoadingState private constructor(
    val loadingStatus: LoadingStatus,
    val errorMessage: String? = null
){
    companion object {
        val COMPLETE = PagingLoadingState(LoadingStatus.SUCCESS)
        val LOADING = PagingLoadingState(LoadingStatus.LOADING)
        fun ERROR(errorMessage: String?) = PagingLoadingState(LoadingStatus.FAILED, errorMessage)
        fun LOADING_WITH_ERROR(errorMessage: String?) = PagingLoadingState(LoadingStatus.LOADING_WITH_ERROR, errorMessage)
    }
}

enum class LoadingStatus {
    LOADING,
    SUCCESS,
    FAILED,
    LOADING_WITH_ERROR
}