package com.example.coremodule.navigation

import androidx.fragment.app.Fragment

interface Router {

    fun navigateTo(fragment: Fragment)

    fun navigateBack(): Boolean
}