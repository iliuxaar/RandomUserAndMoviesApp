package com.example.randomuserfeature

import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.fragmentfactory.BaseFragmentFactory
import com.example.randomuserfeature.di.MainRandomUserComponent

class RandomUserFragmentFactory(val mainRandomUserComponent: MainRandomUserComponent): BaseFragmentFactory(){

    override fun initFragments(clazz: Class<out Fragment>): Fragment? {
        return if(clazz == RandomUsersFlowFragment::class.java) RandomUsersFlowFragment(mainRandomUserComponent)
        else null
    }
}