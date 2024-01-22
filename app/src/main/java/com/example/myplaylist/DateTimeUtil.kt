package com.example.myplaylist

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {
        private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        fun simpleDateFormat(millis: Long?): String {
                return dateFormatter.format(millis)
        }
}