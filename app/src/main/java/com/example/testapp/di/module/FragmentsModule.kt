package com.example.testapp.di.module

import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.randomuserfeature.di.MainRandomUserComponent
import dagger.Module
import dagger.Provides


@Module
class FragmentsModule {

    @Provides
    fun randomFlow(mainRandomUserComponent: MainRandomUserComponent) = RandomUsersFlowFragment(mainRandomUserComponent)
}