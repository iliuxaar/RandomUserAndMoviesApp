package com.example.randomuserfeature.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.randomuserfeature.BackHandler
import com.example.randomuserfeature.R

abstract class FlowFragment: Fragment(), BackHandler {

    protected abstract val rootFragment: Fragment

    lateinit var router: Router

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flow, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        router = FragmentsRouter(childFragmentManager, R.id.container)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container, rootFragment)
                .commit()
        }
    }

    override fun handleBack(): Boolean {
        return router.navigateBack()
    }

    fun reset() {
        while (childFragmentManager.popBackStackImmediate()) {}
    }

}