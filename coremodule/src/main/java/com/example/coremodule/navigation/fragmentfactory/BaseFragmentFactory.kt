package com.example.coremodule.navigation.fragmentfactory

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlin.reflect.KClass

abstract class BaseFragmentFactory: FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
        val clazz = loadFragmentClass(classLoader, className).kotlin
        val fragment: Fragment? = initFragments(clazz) ?: return super.instantiate(classLoader, className, args)
        if(args != null) fragment?.arguments = args
        return fragment!!
    }

    abstract fun initFragments(clazz: KClass<out Fragment>): Fragment?
}