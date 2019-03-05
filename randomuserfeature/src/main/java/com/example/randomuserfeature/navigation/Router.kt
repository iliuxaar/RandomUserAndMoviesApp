package com.example.randomuserfeature.navigation

import androidx.fragment.app.Fragment

interface Router {

    fun navigateTo(fragment: Fragment)

    fun navigateBack(): Boolean
}