package com.example.randomuserfeature.api.entities

data class Location(val city: String = "",
                    val street: String = "",
                    val timezone: Timezone,
                    val postcode: String = "",
                    val coordinates: Coordinates,
                    val state: String = "")