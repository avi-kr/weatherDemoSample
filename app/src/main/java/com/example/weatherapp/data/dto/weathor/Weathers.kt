package com.example.weatherapp.data.dto.weathor

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@Parcelize
data class Weathers(val list: List<WeatherItem>, val city: WeatherCity) : Parcelable

@Parcelize
data class WeatherCity(val name: String) : Parcelable