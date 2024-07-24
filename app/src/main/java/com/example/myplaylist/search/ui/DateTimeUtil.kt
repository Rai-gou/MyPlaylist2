package com.example.myplaylist.search.ui

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

const val END_TIME = "00:00"
object DateTimeUtil {
        @SuppressLint("ConstantLocale")
        private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        fun simpleDateFormat(millis: Long?): String {
                return if (millis != null) {
                        dateFormatter.format(millis)
                } else {
                        END_TIME
                }
        }
}