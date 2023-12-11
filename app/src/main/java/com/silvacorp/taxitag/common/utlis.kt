package com.silvacorp.taxitag.common

import android.content.Context
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ComponentActivity.getUnixTime(): Long {
    val actualDate = Date()
    val unixTimeMilliseconds = actualDate.time
    return unixTimeMilliseconds / 1000
}

fun Int.unixToDate(format: String = "dd/MM/yyyy HH:mm:ss "): String{
    return try {
        val date = Date(this.toLong() * 1000)
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.format(date)
    }catch (e: Exception){
        e.printStackTrace()
        ""
    }
}