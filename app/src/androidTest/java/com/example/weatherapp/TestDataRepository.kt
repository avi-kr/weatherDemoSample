package com.example.weatherapp

import com.example.weatherapp.TestUtil.dataStatus
import com.example.weatherapp.TestUtil.initData
import com.example.weatherapp.data.DataRepositorySource
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.Resource.DataError
import com.example.weatherapp.data.Resource.Success
import com.example.weatherapp.data.dto.login.LoginRequest
import com.example.weatherapp.data.dto.login.LoginResponse
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.error.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
class TestDataRepository @Inject constructor() : DataRepositorySource {

    override suspend fun requestWeathers(): Flow<Resource<Weathers>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(DataError(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Success(Weathers(arrayListOf()))) }
            }
        }
    }

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(
                Success(
                    LoginResponse(
                        "1", "Abhishek", "Kumar",
                        "New Delhi", "111", "110096", "Delhi",
                        "India", "abhi@abhi.abhi"
                    )
                )
            )
        }
    }
}