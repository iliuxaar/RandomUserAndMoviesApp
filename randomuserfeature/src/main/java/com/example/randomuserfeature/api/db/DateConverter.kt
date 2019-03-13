package com.example.randomuserfeature.api.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    companion object {
        @JvmStatic val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        @JvmStatic
        @TypeConverter
        fun fromDate(date: Date) = SimpleDateFormat(DATE_FORMAT, Locale.US).format(date)

        @JvmStatic
        @TypeConverter
        fun fromString(string: String) = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(string)
    }

}