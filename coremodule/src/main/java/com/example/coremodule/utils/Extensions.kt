package com.example.coremodule.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.coremodule.navigation.fragmentfactory.BaseFragmentFactory

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(this.context).inflate(layoutId, this, false)
}

fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun FragmentManager.addFactoryWithTransaction(baseFragmentFactory: BaseFragmentFactory): FragmentTransaction {
    this.fragmentFactory = baseFragmentFactory
    return this.beginTransaction()
}