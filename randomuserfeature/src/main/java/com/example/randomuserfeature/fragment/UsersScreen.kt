package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomuserfeature.R
import com.example.randomuserfeature.Screen
import com.example.randomuserfeature.presentationmodel.ToastMessage
import com.example.randomuserfeature.presentationmodel.UsersPresentationModel
import kotlinx.android.synthetic.main.users_layout.*
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler

class UsersScreen: Screen<UsersPresentationModel>(), NavigationMessageHandler {

    private val usersAdapter = UsersAdapter { result ->
        presentationModel.userClick.consumer.accept(result)
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
    }

    override fun onBindPresentationModel(pm: UsersPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.result bindTo { usersAdapter.setUsers(it) }
    }

    override fun handleNavigationMessage(message: NavigationMessage): Boolean {
        when (message) {
            is ToastMessage -> Toast.makeText(context, "Bla", Toast.LENGTH_LONG).show()
        }
        return true
    }
}