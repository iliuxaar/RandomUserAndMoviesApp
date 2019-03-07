package com.example.coremodule.navigation.fragmentfactory

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

abstract class BaseFragmentFactory: FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
        val clazz = loadFragmentClass(classLoader, className)
        val fragment: Fragment? = initFragments(clazz) ?: return super.instantiate(classLoader, className, args)
        if(args != null) fragment?.arguments = args
        return fragment!!
    }

    abstract fun initFragments(clazz: Class<out Fragment>): Fragment?
}