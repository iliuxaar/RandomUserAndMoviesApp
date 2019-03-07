package com.example.randomuserfeature

import com.example.randomuserfeature.api.entities.ResultsItem
import me.dmdev.rxpm.navigation.NavigationMessage

internal class UserDetailsMessage(val userInfo: ResultsItem): NavigationMessage
