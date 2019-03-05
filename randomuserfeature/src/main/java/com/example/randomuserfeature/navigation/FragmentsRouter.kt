package com.example.randomuserfeature.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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
            .addToBackStack(null)
            .commit()
    }

    override fun navigateBack(): Boolean {
        return mFragmentManager.popBackStackImmediate()
    }
}
