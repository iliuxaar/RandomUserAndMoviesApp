package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coremodule.navigation.RouterProvider
import com.example.coremodule.utils.setVisibility
import com.example.randomuserfeature.R
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.presentationmodel.UsersPresentationModel
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import kotlinx.android.synthetic.main.retry_widget.view.*
import kotlinx.android.synthetic.main.users_layout.*
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler

class UsersScreen: com.example.coremodule.pm.Screen<UsersPresentationModel>(), NavigationMessageHandler {

    private val usersAdapter = UsersAdapter { result ->
        presentationModel.userItemClick.consumer.accept(result)
    }

    override val screenLayout = R.layout.users_layout

    override fun providePresentationModel(): UsersPresentationModel = UsersPresentationModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(users_list) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter
        }
        swipe_container.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    override fun onBindPresentationModel(pm: UsersPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.loadedResult bindTo {
            users_list.setVisibility(true)
            usersAdapter.addUsers(it)
        }

        pm.isLoading bindTo {
            swipe_container.isRefreshing = it
        }

        pm.isError bindTo {
            retryWidget.setVisibility(it)
            users_list.setVisibility(!it)
        }

        retryWidget.retryButton.clicks().bindTo(pm.retryButtonClick.consumer)

        users_list.scrollEvents().bindTo(pm.scrollListAction.consumer)

        swipe_container.refreshes().bindTo(pm.refreshUsersAction.consumer)
    }

    override fun handleNavigationMessage(message: NavigationMessage): Boolean {
        when (message) {
            is UserDetailsMessage -> RouterProvider.router.navigateTo(UserDetailsScreen.createInstance(message.userInfo.id.value ?: "null"))
        }
        return true
    }
}