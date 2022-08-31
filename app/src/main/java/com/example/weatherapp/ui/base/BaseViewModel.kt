package com.example.weatherapp.ui.base

import androidx.lifecycle.ViewModel
import com.example.weatherapp.usecase.errors.ErrorManager
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
abstract class BaseViewModel : ViewModel() {

    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}