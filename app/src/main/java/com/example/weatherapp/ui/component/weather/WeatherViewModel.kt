package com.example.weatherapp.ui.component.weather

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.DataRepositorySource
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.ui.base.BaseViewModel
import com.example.weatherapp.utils.SingleEvent
import com.example.weatherapp.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@HiltViewModel
class WeatherViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val weathersLiveDataPrivate = MutableLiveData<Resource<Weathers>>()
    val weathersLiveData: LiveData<Resource<Weathers>> get() = weathersLiveDataPrivate


    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val weatherSearchFoundPrivate: MutableLiveData<WeatherItem> = MutableLiveData()
    val weatherSearchFound: LiveData<WeatherItem> get() = weatherSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openWeatherDetailsPrivate = MutableLiveData<SingleEvent<WeatherItem>>()
    val openWeatherDetails: LiveData<SingleEvent<WeatherItem>> get() = openWeatherDetailsPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getWeathers() {
        viewModelScope.launch {
            weathersLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestWeathers().collect {
                    weathersLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openWeatherDetails(weather: WeatherItem) {
        openWeatherDetailsPrivate.value = SingleEvent(weather)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

}