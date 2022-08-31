package com.example.weatherapp.task.ui.component.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.DataRepository
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.error.NETWORK_ERROR
import com.example.weatherapp.ui.component.weather.WeatherViewModel
import com.example.weatherapp.utils.InstantExecutorExtension
import com.example.weatherapp.utils.MainCoroutineRule
import com.example.weatherapp.utils.TestModelsGenerator
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.*
import org.junit.Assert.*
import org.junit.jupiter.api.extension.*

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class WeathersViewModelTest {

    // Subject under test
    private lateinit var weatherViewModel: WeatherViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherTitle: String
    private val testModelsGenerator: TestModelsGenerator = TestModelsGenerator()

    @Before
    fun setUp() {
        // Create class under test
        // We initialise the repository with no tasks
        weatherTitle = testModelsGenerator.getStubSearchTitle()
    }

    @Test
    fun `get Weathers List`() {
        // Let's do an answer for the liveData
        val weathersModel = testModelsGenerator.generateWeathers()

        //1- Mock calls
        coEvery { dataRepository.requestWeathers() } returns flow {
            emit(Resource.Success(weathersModel))
        }

        //2-Call
        weatherViewModel = WeatherViewModel(dataRepository)
        weatherViewModel.getWeathers()
        //active observer for livedata
        weatherViewModel.weathersLiveData.observeForever { }

        //3-verify
        val isEmptyList = weatherViewModel.weathersLiveData.value?.data?.list.isNullOrEmpty()
        assertEquals(weathersModel, weatherViewModel.weathersLiveData.value?.data)
        Assert.assertEquals(false, isEmptyList)
    }

    @Test
    fun `get Weathers Empty List`() {
        // Let's do an answer for the liveData
        val weathersModel = testModelsGenerator.generateWeathersModelWithEmptyList()

        //1- Mock calls
        coEvery { dataRepository.requestWeathers() } returns flow {
            emit(Resource.Success(weathersModel))
        }

        //2-Call
        weatherViewModel = WeatherViewModel(dataRepository)
        weatherViewModel.getWeathers()
        //active observer for livedata
        weatherViewModel.weathersLiveData.observeForever { }

        //3-verify
        val isEmptyList = weatherViewModel.weathersLiveData.value?.data?.list.isNullOrEmpty()
        assertEquals(weathersModel, weatherViewModel.weathersLiveData.value?.data)
        Assert.assertEquals(true, isEmptyList)
    }

    @Test
    fun `get Weathers Error`() {
        // Let's do an answer for the liveData
        val error: Resource<Weathers> = Resource.DataError(NETWORK_ERROR)

        //1- Mock calls
        coEvery { dataRepository.requestWeathers() } returns flow {
            emit(error)
        }

        //2-Call
        weatherViewModel = WeatherViewModel(dataRepository)
        weatherViewModel.getWeathers()
        //active observer for livedata
        weatherViewModel.weathersLiveData.observeForever { }

        //3-verify
        assertEquals(NETWORK_ERROR, weatherViewModel.weathersLiveData.value?.errorCode)
    }
}