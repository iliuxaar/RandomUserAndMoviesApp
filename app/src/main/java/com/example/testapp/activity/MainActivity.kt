package com.example.testapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.coremodule.navigation.FlowFragment
import com.example.coremodule.navigation.Router
import com.example.coremodule.navigation.RouterProvider
import com.example.coremodule.utils.addFactoryWithTransaction
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.testapp.App
import com.example.testapp.R
import com.example.testapp.fragmentfactory.FlowFragmentsFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), com.example.coremodule.pm.BackHandler {

    @Inject lateinit var flowFragmentsFactory: FlowFragmentsFactory
    private lateinit var randomUsersFlowFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        App.component.inject(this)
        supportFragmentManager.fragmentFactory = flowFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        initRouter()
        initFragments(savedInstanceState)
    }

    private fun initFragments(savedInstanceState: Bundle?){
        if(savedInstanceState == null) {
            randomUsersFlowFragment = flowFragmentsFactory.instantiate(classLoader, RandomUsersFlowFragment::class.java.name, Bundle())
            supportFragmentManager.addFactoryWithTransaction(flowFragmentsFactory)
                .add(R.id.main_container, randomUsersFlowFragment, RANDOM_USER_TAG)
                .commit()
        } else {
            randomUsersFlowFragment = supportFragmentManager.findFragmentByTag(RANDOM_USER_TAG) as RandomUsersFlowFragment
        }
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
            R.id.navigation_random_user -> return randomUsersFlowFragment as RandomUsersFlowFragment
            else -> throw IllegalStateException("There should be one of the flow fragments")
        }
    }

    override fun handleBack() = getCurrentFlow().handleBack()

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

    override fun onBackPressed() {
        if(!handleBack()) super.onBackPressed()
    }

    companion object {
        private const val RANDOM_USER_TAG = "random_user"
    }
}
