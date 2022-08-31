package com.example.weatherapp.ui.component.weather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.error.SEARCH_ERROR
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.ui.base.BaseActivity
import com.example.weatherapp.utils.SingleEvent
import com.example.weatherapp.utils.observe
import com.example.weatherapp.utils.observeEvent
import com.example.weatherapp.utils.setupSnackbar
import com.example.weatherapp.utils.showToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : BaseActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private val weathersListViewModel: WeatherViewModel by viewModels()

    override fun observeViewModel() {
        observe(weathersListViewModel.weathersLiveData, ::handleWeathersList)
        observe(weathersListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(weathersListViewModel.openWeatherDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(weathersListViewModel.showSnackBar)
        observeToast(weathersListViewModel.showToast)
    }

    private fun showLoadingView() {

    }

    private fun showSearchResult(weathersItem: WeatherItem) {
        weathersListViewModel.openWeatherDetails(weathersItem)
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()

    }

    private fun showSearchError() {
        weathersListViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showDataView(show: Boolean) {

    }

    private fun bindListData(weathers: Weathers) {

    }

    private fun handleWeathersList(status: Resource<Weathers>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(weathers = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { weathersListViewModel.showToastMessage(it) }
            }
        }
    }

    override fun initViewBinding() {
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<WeatherItem>) {
        navigateEvent.getContentIfNotHandled()?.let {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weathersListViewModel.getWeathers()
    }
}