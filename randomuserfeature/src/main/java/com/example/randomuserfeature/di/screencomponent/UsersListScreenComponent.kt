package com.example.randomuserfeature.di.screencomponent

import com.example.randomuserfeature.fragment.UsersListScreen
import dagger.Subcomponent

@Subcomponent
interface UsersListScreenComponent {
    fun inject(usersScreen: UsersListScreen)
}