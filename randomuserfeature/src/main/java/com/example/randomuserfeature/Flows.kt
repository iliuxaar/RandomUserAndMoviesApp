package com.example.randomuserfeature

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.FlowFragment
import com.example.randomuserfeature.di.MainRandomUserComponent
import com.example.randomuserfeature.fragment.UsersListScreen

@SuppressLint("ValidFragment")
class RandomUsersFlowFragment(
    private val mainRandomUserComponent: MainRandomUserComponent
): FlowFragment() {

    override val rootFragment: Fragment
        get() = UsersListScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainComponent = mainRandomUserComponent
    }

    companion object{

        private lateinit var mainComponent: MainRandomUserComponent

        fun plusUsersComponent() = mainComponent.plusUsersScreenComponent()
        fun plusUserDetailsComponent() = mainComponent.plusUserDetailsScreenComponent()

    }

}