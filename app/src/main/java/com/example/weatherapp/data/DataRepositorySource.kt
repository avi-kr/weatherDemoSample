package com.example.weatherapp.data

import com.example.weatherapp.data.dto.login.LoginRequest
import com.example.weatherapp.data.dto.login.LoginResponse
import com.example.weatherapp.data.dto.weathor.Weathers
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
interface DataRepositorySource {

    suspend fun requestWeathers(): Flow<Resource<Weathers>>
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>
}