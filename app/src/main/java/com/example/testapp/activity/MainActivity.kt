package com.example.testapp.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.FlowFragment
import com.example.coremodule.navigation.Router
import com.example.coremodule.navigation.RouterProvider
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.testapp.App
import com.example.testapp.R
import com.example.testapp.factory.FlowFragmentsFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var flowFragmentsFactory: FlowFragmentsFactory
    private lateinit var randomUsersFlowFragment: Fragment

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        App.component.inject(this)
        supportFragmentManager.fragmentFactory = flowFragmentsFactory
        super.onCreate(savedInstanceState)

        initBottomNavigation()
        initRouter()
        initFragments(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        showFlowFragments()
    }

    private fun initFragments(savedInstanceState: Bundle?){
        if(savedInstanceState == null) {
            randomUsersFlowFragment = flowFragmentsFactory.instantiate(classLoader, RandomUsersFlowFragment::class.java.name, Bundle())
        } else {
            randomUsersFlowFragment = supportFragmentManager.findFragmentByTag(RANDOM_USER_TAG) as RandomUsersFlowFragment
        }
    }

    private fun showFlowFragments(){
        if (supportFragmentManager.findFragmentByTag(RANDOM_USER_TAG) == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, randomUsersFlowFragment, RANDOM_USER_TAG)
                .commit()
    }

    private fun initBottomNavigation(){
        navigation.setOnNavigationItemSelectedListener {
            selectFragment(it.itemId)
            true
        }
        navigation.setOnNavigationItemReselectedListener { flowFromId(it.itemId).reset() }
    }

    private fun flowFromId(tabId: Int): FlowFragment {
        when(tabId) {
            R.id.navigation_random_user -> return randomUsersFlowFragment as RandomUsersFlowFragment
            else -> throw IllegalStateException("There should be one of the flow fragments")
        }
    }

    private fun getCurrentFlow(): FlowFragment = flowFromId(navigation.selectedItemId)

    private fun selectFragment(id: Int){
        when(id){
            R.id.navigation_random_user -> {
                supportFragmentManager.beginTransaction()
                    .show(randomUsersFlowFragment)
                    .commit()
            }
            R.id.navigation_movies -> {
                supportFragmentManager.beginTransaction()
                    .hide(randomUsersFlowFragment)
                    .commit()
            }
        }
    }

    private fun initRouter(){
        val mRouter = object : Router {
            override fun navigateTo(fragment: Fragment) {
                getCurrentFlow().router.navigateTo(fragment)
            }

            override fun navigateBack(): Boolean {
                return getCurrentFlow().router.navigateBack()
            }
        }
        RouterProvider.registerRouter(mRouter)
    }

    override fun handleBack() = getCurrentFlow().handleBack()

    companion object {
        private const val RANDOM_USER_TAG = "random_user"
    }
}
