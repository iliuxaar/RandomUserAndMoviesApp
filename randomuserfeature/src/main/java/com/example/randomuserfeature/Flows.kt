package com.example.randomuserfeature

import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.FlowFragment
import com.example.randomuserfeature.fragment.UsersScreen

class RandomUsersFlowFragment: FlowFragment() {
    override val rootFragment: Fragment
        get() = UsersScreen()
}