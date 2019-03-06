package com.example.testapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.FlowFragment
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.coremodule.navigation.Router
import com.example.coremodule.navigation.RouterProvider
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), com.example.coremodule.pm.BackHandler {

    private lateinit var randomUsersFlowFragment: RandomUsersFlowFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        initRouter()
        initFragments()
    }

    private fun initFragments(){
        randomUsersFlowFragment = RandomUsersFlowFragment()
        supportFragmentManager.beginTransaction()
            .add(
                R.id.main_container, randomUsersFlowFragment,
                RANDOM_USER_TAG
            )
            .hide(randomUsersFlowFragment)
            .commit()
    }

    private fun initBottomNavigation(){
        navigation.setOnNavigationItemSelectedListener {
            selectFragment(it.itemId)
            true
        }
        navigation.setOnNavigationItemReselectedListener { flowFromId(it.itemId).reset() }
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

    private fun flowFromId(tabId: Int): FlowFragment {
        when(tabId) {
            R.id.navigation_notifications -> return randomUsersFlowFragment
            else -> throw IllegalStateException("There should be one of the flow fragments")
        }
    }

    override fun handleBack() = getCurrentFlow().handleBack()

    private fun getCurrentFlow(): FlowFragment = flowFromId(navigation.selectedItemId)

    private fun selectFragment(id: Int){
        when(id){
            R.id.navigation_notifications -> {
                supportFragmentManager.beginTransaction()
                    .show(randomUsersFlowFragment)
                    .commit()
            }
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .hide(randomUsersFlowFragment)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        if(!handleBack()) super.onBackPressed()
    }

    companion object {
        private const val RANDOM_USER_TAG = "random_user"
    }
}
