package com.example.randomuserfeature.di.screencomponent

import com.example.randomuserfeature.fragment.UserDetailsScreen
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface UserDetailsScreenComponent {

    fun inject(userDetailsScreen: UserDetailsScreen)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun userId(userId: String): Builder
        fun build(): UserDetailsScreenComponent
    }
}