package com.example.weatherapp.data.dto.login

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
data class LoginResponse(
    val id: String, val firstName: String, val lastName: String,
    val streetName: String, val buildingNumber: String,
    val postalCode: String, val state: String,
    val country: String, val email: String
)
