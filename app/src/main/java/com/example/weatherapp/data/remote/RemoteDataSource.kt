package com.example.weatherapp.data.remote

import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.Weathers

internal interface RemoteDataSource {

    suspend fun requestWeathers(lat: Double, log: Double): Resource<Weathers>
}