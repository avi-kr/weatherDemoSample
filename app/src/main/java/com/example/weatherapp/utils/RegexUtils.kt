package com.example.weatherapp.utils

import java.util.regex.Pattern

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
object RegexUtils {

    private val EMAIL_ADDRESS: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmail(email: String): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }
}