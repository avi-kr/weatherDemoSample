package com.example.weatherapp

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
sealed class DataStatus {

    object Success : DataStatus()
    object Fail : DataStatus()
    object EmptyResponse : DataStatus()
}