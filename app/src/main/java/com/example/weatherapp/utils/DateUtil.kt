package com.example.weatherapp.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Abhishek Kumar on 02,September,2022
 * (c)2022 VMock, India. All rights reserved.
 */
object DateUtil {

    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(originalFormat: String, dateInString: String, newFormat: String): String {
        val originalFormat: DateFormat = SimpleDateFormat(originalFormat, Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat(newFormat)
        val date: Date = originalFormat.parse(dateInString) as Date
        return targetFormat.format(date)
    }

}