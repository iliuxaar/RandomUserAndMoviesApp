package com.example.randomuserfeature.fragment

import android.os.Bundle
import com.example.coremodule.pm.Screen
import com.example.randomuserfeature.R
import com.example.randomuserfeature.RandomUsersFlowFragment
import com.example.randomuserfeature.presentationmodel.UserDetailsPresentationModel
import kotlinx.android.synthetic.main.user_detail_fragment_layout.*
import javax.inject.Inject

class UserDetailsScreen: Screen<UserDetailsPresentationModel>() {

    @Inject lateinit var userDetailsPresentationModel: UserDetailsPresentationModel

    companion object {
        private const val ID = "valueId"
        fun createInstance(id: Long) = UserDetailsScreen().apply {
            arguments = Bundle().apply { putLong(ID, id) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        RandomUsersFlowFragment.plusUserDetailsComponent().userId(arguments!!.getLong(ID)).build().inject(this)
        super.onCreate(savedInstanceState)
    }

    override val screenLayout = R.layout.user_detail_fragment_layout

    override fun providePresentationModel() = userDetailsPresentationModel

    override fun onBindPresentationModel(pm: UserDetailsPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.userResult bindTo {
            secondScreenTextView.text = it.login
        }
    }
}