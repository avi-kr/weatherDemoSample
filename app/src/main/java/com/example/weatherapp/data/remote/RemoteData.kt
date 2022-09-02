package com.example.weatherapp.data.remote

import android.util.Log
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.error.NETWORK_ERROR
import com.example.weatherapp.data.error.NO_INTERNET_CONNECTION
import com.example.weatherapp.utils.NetworkConnectivity
import com.example.weatherapp.data.remote.service.WeatherService
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) :
    RemoteDataSource {

    override suspend fun requestWeathers(lat: Double, log: Double): Resource<Weathers> {
        val weathersService = serviceGenerator.createService(WeatherService::class.java)
        return when (val response = processCall({ weathersService.fetchWeathers(lat, log) })) {
            is Weathers -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}