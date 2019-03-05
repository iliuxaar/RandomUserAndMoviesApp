package com.example.randomuserfeature

import com.example.randomuserfeature.api.entities.ResultsItem
import me.dmdev.rxpm.navigation.NavigationMessage

class BackMessage: NavigationMessage
class UserDetailsMessage(val userInfo: ResultsItem): NavigationMessage
