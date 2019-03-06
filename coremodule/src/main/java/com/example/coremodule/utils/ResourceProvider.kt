package com.example.coremodule.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context){

    fun getString(@StringRes stringId: Int) = context.resources.getString(stringId)

    fun getString(@StringRes stringId: Int, vararg formatArgs: Any) = context.resources.getString(stringId, *formatArgs)

    //TODO: there can be placed another resource getters, like getColor, getDimens, getDrawable etc.
}