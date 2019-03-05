package com.example.randomuserfeature.fragment

import android.os.Bundle
import android.util.Log
import com.example.randomuserfeature.R
import com.example.randomuserfeature.Screen
import com.example.randomuserfeature.presentationmodel.UserDetailsPresentationModel
import kotlinx.android.synthetic.main.user_detail_fragment_layout.*

class UserDetailsScreen: Screen<UserDetailsPresentationModel>() {

    companion object {
        private const val ID = "valueId"
        fun createInstance(valueId: String) = UserDetailsScreen().apply {
            arguments = Bundle().apply { putString(ID, valueId) }
            Log.d("test", "id is $valueId")
        }
    }

    override val screenLayout = R.layout.user_detail_fragment_layout

    override fun providePresentationModel(): UserDetailsPresentationModel {
        return UserDetailsPresentationModel(
            arguments!!.getString(ID)!!
        )
    }

    override fun onBindPresentationModel(pm: UserDetailsPresentationModel) {
        super.onBindPresentationModel(pm)

        pm.userResult bindTo {
            textView2.text = it.name.first
        }
    }
}