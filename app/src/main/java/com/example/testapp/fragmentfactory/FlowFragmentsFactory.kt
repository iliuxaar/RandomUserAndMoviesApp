package com.example.testapp. fragmentfactory

import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.fragmentfactory.BaseFragmentFactory
import com.example.randomuserfeature.RandomUsersFlowFragment
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class FlowFragmentsFactory @Inject constructor(
    val randomUserFlowProvider: Provider<RandomUsersFlowFragment>
    // add another flowFragmentsProviders there
): BaseFragmentFactory() {

    override fun initFragments(clazz: KClass<out Fragment>): Fragment? {
        return if(clazz == RandomUsersFlowFragment::class) randomUserFlowProvider.get()
        // add other flow fragments there
        else null
    }


}