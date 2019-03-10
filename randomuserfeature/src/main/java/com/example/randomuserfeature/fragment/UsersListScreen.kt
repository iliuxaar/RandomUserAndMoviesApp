package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coremodule.navigation.Router
import com.example.coremodule.pm.Screen
import com.example.randomuserfeature.R
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.randomuserfeature.UserDetailsMessage
import com.example.randomuserfeature.presentationmodel.UsersListPresentationModel
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import kotlinx.android.synthetic.main.users_layout.*
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler
import javax.inject.Inject

class UsersListScreen: Screen<UsersListPresentationModel>(), NavigationMessageHandler {

    @Inject lateinit var userPresentationModel: UsersListPresentationModel
    @Inject lateinit var router: Router

    private val usersAdapter = UsersAdapter { result ->
        presentationModel.userItemClick.consumer.accept(result)
    }

    override val screenLayout = R.layout.users_layout

    override fun providePresentationModel() = userPresentationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        RandomUsersFlowFragment.plusUsersComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(users_list) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter
        }
        swipe_container.setSwipeableChildren(users_list.id)
    }

    override fun onBindPresentationModel(pm: UsersListPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.loadedResult bindTo { usersAdapter.addUsers(it) }

        pm.isLoading bindTo { swipe_container.isRefreshing = it }

        users_list.scrollEvents().bindTo(pm.scrollListAction.consumer)

        swipe_container.refreshes().doOnNext { usersAdapter.clearItems() }.bindTo(pm.refreshUsersAction.consumer)
    }

    override fun handleNavigationMessage(message: NavigationMessage): Boolean {
        when (message) {
            is UserDetailsMessage -> router.navigateTo(UserDetailsScreen.createInstance(message.userInfo.id.value ?: "null"))
        }
        return true
    }
}