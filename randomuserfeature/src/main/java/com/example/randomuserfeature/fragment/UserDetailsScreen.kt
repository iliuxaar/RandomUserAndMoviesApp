package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.util.Log
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
        fun createInstance(valueId: String) = UserDetailsScreen().apply {
            arguments = Bundle().apply { putString(ID, valueId) }
            Log.d("test", "id is $valueId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        RandomUsersFlowFragment.plusUserDetailsComponent().userId(arguments!!.getString(ID)).build().inject(this)
        super.onCreate(savedInstanceState)
    }

    override val screenLayout = R.layout.user_detail_fragment_layout

    override fun providePresentationModel() = userDetailsPresentationModel

    override fun onBindPresentationModel(pm: UserDetailsPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.userResult bindTo {
            secondScreenTextView.text = it.name.first
        }
    }
}