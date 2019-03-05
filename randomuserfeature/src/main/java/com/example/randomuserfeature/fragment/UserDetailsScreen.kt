package com.example.randomuserfeature.fragment

import com.example.randomuserfeature.R
import com.example.randomuserfeature.Screen
import com.example.randomuserfeature.presentationmodel.UserDetailsPresentationModel

class UserDetailsScreen: Screen<UserDetailsPresentationModel>() {

    override val screenLayout = R.layout.user_detail_fragment_layout

    override fun providePresentationModel(): UserDetailsPresentationModel = UserDetailsPresentationModel()
}