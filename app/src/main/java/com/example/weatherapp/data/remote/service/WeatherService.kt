package com.example.weatherapp.data.remote.service

import com.example.weatherapp.WEATHER_KEY
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("forecast?&appid=$WEATHER_KEY&units=metric")
    suspend fun fetchWeathers(@Query("lat") lat: Double, @Query("lon") log: Double): Response<Weathers>
}
