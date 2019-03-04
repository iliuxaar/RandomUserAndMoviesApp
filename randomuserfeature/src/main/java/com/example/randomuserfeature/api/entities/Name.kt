package com.example.randomuserfeature.api.entities

data class Name(val last: String = "",
                val title: String = "",
                val first: String = ""){

    override fun toString(): String {
        return "$title ${last.capitalize()} ${first.capitalize()}"
    }
}