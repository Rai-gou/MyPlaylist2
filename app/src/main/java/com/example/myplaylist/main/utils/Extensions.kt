package com.example.myplaylist.main.utils

// Extension function to convert milliseconds to minutes
fun Long.toMinutes(): Long {
    return this / 1000 / 60
}