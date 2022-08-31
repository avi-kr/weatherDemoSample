package com.example.weatherapp.data.local

import android.content.Context
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.login.LoginRequest
import com.example.weatherapp.data.dto.login.LoginResponse
import com.example.weatherapp.data.error.PASS_WORD_ERROR
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
class LocalData @Inject constructor(val context: Context) {

    fun doLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        if (loginRequest == LoginRequest("abhi@abhi.abhi", "abhi")) {
            return Resource.Success(
                LoginResponse(
                    "1", "Abhishek", "Kumar",
                    "New Delhi", "111", "110096", "Delhi",
                    "India", "abhi@abhi.abhi"
                )
            )
        }
        return Resource.DataError(PASS_WORD_ERROR)
    }
}