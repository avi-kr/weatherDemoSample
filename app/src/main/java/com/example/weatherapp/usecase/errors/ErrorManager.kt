package com.example.weatherapp.usecase.errors

import com.example.weatherapp.data.error.Error
import com.example.weatherapp.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {

    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}