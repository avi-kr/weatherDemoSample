package com.example.weatherapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}

fun <T> LifecycleOwner.observeEvent(liveData: LiveData<SingleEvent<T>>, action: (t: SingleEvent<T>) -> Unit) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}
