package com.example.randomuserfeature.api.entities

data class ResultsItem(val nat: String = "",
                       val gender: String = "",
                       val phone: String = "",
                       val dob: Dob,
                       val name: Name,
                       val id: Id,
                       val location: Location,
                       val cell: String = "",
                       val email: String = "",
                       val picture: Picture
)