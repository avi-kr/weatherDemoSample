package com.example.weatherapp.usecase.errors

import com.example.weatherapp.data.error.Error

interface ErrorUseCase {

    fun getError(errorCode: Int): Error
}