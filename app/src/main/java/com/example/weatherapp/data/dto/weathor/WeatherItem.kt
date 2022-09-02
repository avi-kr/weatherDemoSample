package com.example.weatherapp.data.dto.weathor

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@JsonClass(generateAdapter = false)
@Parcelize
data class WeatherItem(
    val main: MainWeatherItem,
    val weather: List<WeatherCondition> = listOf(),
    val wind: WindCondition = WindCondition(),
    val visibility: String,
    val pop: String,
    @Json(name = "dt_txt")
    val dtTxt: String
) : Parcelable

@Parcelize
data class MainWeatherItem(
    val temp: String = "",
    @Json(name = "temp_min")
    val tempMin: String = "",
    @Json(name = "temp_max")
    val tempMax: String = "",
    val humidity: String = "",
    val pressure: String = ""
) : Parcelable

@Parcelize
data class WeatherCondition(
    val main: String = ""
) : Parcelable

@Parcelize
data class WindCondition(
    val speed: String = ""
) : Parcelable