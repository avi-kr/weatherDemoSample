package com.example.weatherapp.data.remote.service

import com.example.weatherapp.WEATHER_KEY
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import retrofit2.Response
import retrofit2.http.GET

interface WeatherService {

    @GET("forecast?lat=28.459497&lon=77.026634&appid=$WEATHER_KEY")
    suspend fun fetchWeathers(): Response<Weathers>
}
