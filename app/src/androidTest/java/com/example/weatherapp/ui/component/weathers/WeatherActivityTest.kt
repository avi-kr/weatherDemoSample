package com.example.weatherapp.ui.component.weathers

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.rule.ActivityTestRule
import com.example.weatherapp.DataStatus
import com.example.weatherapp.TestUtil.dataStatus
import com.example.weatherapp.ui.component.weather.WeatherActivity
import com.example.weatherapp.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.*

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@HiltAndroidTest
class WeatherActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(WeatherActivity::class.java, false, false)
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun displayWeathersList() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
    }

    @Test
    fun testRefresh() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
    }

    @Test
    fun noData() {
        dataStatus = DataStatus.Fail
        mActivityTestRule.launchActivity(null)
    }

    @Test
    fun testSearch() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
    }

    @Test
    fun testNoSearchResult() {
    }

    @Test
    fun testScroll() {
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}