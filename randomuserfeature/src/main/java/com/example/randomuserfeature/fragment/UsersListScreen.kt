package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coremodule.navigation.Router
import com.example.coremodule.pm.Screen
import com.example.randomuserfeature.R
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.data.LoadingStatus
import com.example.randomuserfeature.data.PagingLoadingState
import com.example.randomuserfeature.presentationmodel.UsersListPresentationModel
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.widget.checkedChanges
import kotlinx.android.synthetic.main.item_network_state.*
import kotlinx.android.synthetic.main.users_layout.*
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler
import javax.inject.Inject

class UsersListScreen: Screen<UsersListPresentationModel>(), NavigationMessageHandler {

    @Inject lateinit var userPresentationModel: UsersListPresentationModel
    @Inject lateinit var router: Router

    private val usersAdapter = UsersListAdapter (
        { presentationModel.userItemClick.consumer.accept(it) },
        { presentationModel.retryActionClick.consumer.accept(Unit) },
        { presentationModel.saveActionClick.consumer.accept(it) }
    )

    override val screenLayout = R.layout.users_layout

    override fun providePresentationModel() = userPresentationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        RandomUsersFlowFragment.plusUsersComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(usersList) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter
        }
        swipeRefresher.setSwipeableChildren(usersList.id)
    }

    override fun onBindPresentationModel(pm: UsersListPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.isPagedListReady bindTo { usersAdapter.submitList(it)}
        pm.isSwipeLoading bindTo { swipeRefresher.isRefreshing = it }
        pm.isListLoading.bindTo { usersAdapter.setLoadingState(it) }
        pm.isInitialLoad bindTo {
            if(usersAdapter.run { currentList != null && currentList!!.size > 0 }){
                swipeRefresher.isRefreshing = it.loadingStatus == LoadingStatus.LOADING
            } else setInitialLoadingState(it)
        }
        pm.isUserInsertedToDb bindTo { usersAdapter.onAddUserToDb(it) }

        dbCheckBox.checkedChanges().bindTo { pm.shouldUseDbClick.consumer.accept(it) }
        swipeRefresher.refreshes().bindTo(pm.refreshUsersAction.consumer)
    }

    private fun setInitialLoadingState(pagingLoadingState: PagingLoadingState){
        errorMessageTextView.visibility = if (pagingLoadingState.errorMessage != null) View.VISIBLE else View.GONE
        if (pagingLoadingState.errorMessage != null) {
            errorMessageTextView.text = pagingLoadingState.errorMessage
        }

        retryLoadingButton.visibility = if (pagingLoadingState.loadingStatus == LoadingStatus.FAILED) View.VISIBLE else View.GONE
        loadingProgressBar.visibility = if (pagingLoadingState.loadingStatus == LoadingStatus.LOADING) View.VISIBLE else View.GONE

        if (pagingLoadingState.loadingStatus == LoadingStatus.LOADING_WITH_ERROR) {
            retryLoadingButton.visibility = View.VISIBLE
            loadingProgressBar.visibility = View.VISIBLE
        }

        swipeRefresher.isEnabled = pagingLoadingState.loadingStatus == LoadingStatus.SUCCESS

        retryLoadingButton.setOnClickListener { Unit passTo presentationModel.retryActionClick.consumer }
    }


    override fun handleNavigationMessage(message: NavigationMessage): Boolean {
        when (message) {
            is UserDetailsMessage -> router.navigateTo(UserDetailsScreen.createInstance(message.user.id))
        }
        return true
    }
}