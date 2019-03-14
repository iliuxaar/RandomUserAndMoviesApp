package com.example.coremodule.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN

class FragmentsRouter(
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : Router {

    override fun navigateTo(fragment: Fragment) {
        val currentFragment = mFragmentManager.findFragmentById(mContainerId)
        val transaction = mFragmentManager.beginTransaction()

        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        transaction.add(mContainerId, fragment)
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateBack(): Boolean {
        return mFragmentManager.popBackStackImmediate()
    }
}
