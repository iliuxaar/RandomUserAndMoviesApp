package com.example.randomuserfeature

import com.example.randomuserfeature.api.entities.User
import me.dmdev.rxpm.navigation.NavigationMessage

internal class UserDetailsMessage(val user: User): NavigationMessage
