package com.example.weatherapp.data

import com.example.weatherapp.data.dto.login.LoginRequest
import com.example.weatherapp.data.dto.login.LoginResponse
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.local.LocalData
import com.example.weatherapp.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val localRepository: LocalData,
    private val ioDispatcher: CoroutineContext
) : DataRepositorySource {

    override suspend fun requestWeathers(lat: Double, log: Double): Flow<Resource<Weathers>> {
        return flow {
            emit(remoteRepository.requestWeathers(lat, log))
        }.flowOn(ioDispatcher)
    }

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(localRepository.doLogin(loginRequest))
        }.flowOn(ioDispatcher)
    }
}