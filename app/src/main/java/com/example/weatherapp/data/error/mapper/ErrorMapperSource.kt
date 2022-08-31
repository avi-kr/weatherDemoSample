package com.example.weatherapp.data.error.mapper

interface ErrorMapperSource {

    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}