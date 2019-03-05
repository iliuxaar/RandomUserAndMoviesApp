package com.example.randomuserfeature.navigation

import androidx.fragment.app.Fragment
import com.example.randomuserfeature.fragment.UsersScreen

class RandomUsersFlowFragment: FlowFragment() {
    override val rootFragment: Fragment
        get() = UsersScreen()
}