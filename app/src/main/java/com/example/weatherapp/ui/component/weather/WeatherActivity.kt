package com.example.weatherapp.ui.component.weather

import android.Manifest.permission
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import com.example.weatherapp.R
import com.example.weatherapp.R.drawable
import com.example.weatherapp.data.Resource
import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.error.SEARCH_ERROR
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.ui.base.BaseActivity
import com.example.weatherapp.utils.DateUtil.convertDateFormat
import com.example.weatherapp.utils.PermissionUtils
import com.example.weatherapp.utils.SingleEvent
import com.example.weatherapp.utils.observe
import com.example.weatherapp.utils.observeEvent
import com.example.weatherapp.utils.setupSnackbar
import com.example.weatherapp.utils.showToast
import com.example.weatherapp.utils.toGone
import com.example.weatherapp.utils.toVisible
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_bottom_info_container.view.humidity
import kotlinx.android.synthetic.main.main_bottom_info_container.view.probabilityOfPrecipiation
import kotlinx.android.synthetic.main.main_bottom_info_container.view.visibilityTV
import kotlinx.android.synthetic.main.main_bottom_info_container.view.windSpeed
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hour1
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hour2
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hour3
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hourIcon1
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hourIcon2
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.hourIcon3
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.temp1
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.temp2
import kotlinx.android.synthetic.main.main_day_time_weather_container.view.temp3
import kotlinx.android.synthetic.main.main_days_container.view.day1
import kotlinx.android.synthetic.main.main_days_container.view.day2
import kotlinx.android.synthetic.main.main_days_container.view.day3
import kotlinx.android.synthetic.main.main_days_container.view.day4
import kotlinx.android.synthetic.main.main_days_container.view.day5
import kotlinx.android.synthetic.main.main_days_container.view.day6

@AndroidEntryPoint
class WeatherActivity : BaseActivity() {

    private var isDay1Present = false
    private var isDay2Present = false
    private var isDay3Present = false
    private var isDay4Present = false
    private var isDay5Present = false
    private var isDay6Present = false

    private var map: MutableMap<String, MutableList<WeatherItem>> = mutableMapOf()

    private lateinit var binding: ActivityWeatherBinding
    private val weathersListViewModel: WeatherViewModel by viewModels()

    override fun observeViewModel() {
        observe(weathersListViewModel.weathersLiveData, ::handleWeathersList)
        observeEvent(weathersListViewModel.openWeatherDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(weathersListViewModel.showSnackBar)
        observeToast(weathersListViewModel.showToast)
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showDataView(show: Boolean) {
        binding.pbLoading.visibility = View.GONE
    }

    private fun bindWeatherData(weathers: Weathers) {
        binding.pbLoading.visibility = View.GONE

        val weatherlist = weathers.list

        binding.toolbar.title = weathers.city.name
        map = mutableMapOf()

        //map day wise weather data
        mappingDayWiseWeatherData(weatherlist, map)
        initDayTitle(map)
        initDayClicks()
    }

    private fun initDayClicks() {
        binding.root.day1.setOnClickListener {
            dayWiseData(map[binding.root.day1.text]!!.toList())
        }
        binding.root.day2.setOnClickListener {
            dayWiseData(map[binding.root.day2.text]!!.toList())
        }
        binding.root.day3.setOnClickListener {
            dayWiseData(map[binding.root.day3.text]!!.toList())
        }
        binding.root.day4.setOnClickListener {
            dayWiseData(map[binding.root.day4.text]!!.toList())
        }
        binding.root.day5.setOnClickListener {
            dayWiseData(map[binding.root.day5.text]!!.toList())
        }
        binding.root.day6.setOnClickListener {
            dayWiseData(map[binding.root.day6.text]!!.toList())
        }
    }

    private fun dayWiseData(mutableList: List<WeatherItem>) {
        val maxList = ArrayList<Entry>()
        val minList = ArrayList<Entry>()
        val dayTemp = ArrayList<Float>()

        for (i in 0 until mutableList.size) {
            println(i)
            val max = Entry(
                i.toFloat(),
                mutableList[i].main.tempMax.toFloat(),
                ResourcesCompat.getDrawable(resources, drawable.ic_baseline_circle_24, theme)
            )
            val min = Entry(
                i.toFloat(),
                mutableList[i].main.tempMin.toFloat(),
                ResourcesCompat.getDrawable(resources, drawable.ic_baseline_circle_24, theme)
            )
            maxList.add(max)
            minList.add(min)
            dayTemp.add(mutableList[i].main.temp.toFloat())
        }

        setMaxData(maxList)
        setMinData(minList)

        val hourlyList: MutableList<String> = mutableListOf()
        mappingTimeWiseWeatherData(mutableList, hourlyList)
        setHourlyData(hourlyList, dayTemp)

        // draw points over time
        binding.chartMax.animateX(1500)
        binding.chartMin.animateX(1500)

        binding.currentTemp.text = mutableList[0].main.temp.toFloat().toInt().toString()  + "°"
        binding.weatherCondition.text = mutableList[0].weather[0].main
        binding.root.windSpeed.text = mutableList[0].wind.speed
        binding.root.visibilityTV.text = mutableList[0].visibility
        binding.root.humidity.text = mutableList[0].main.humidity
        binding.root.probabilityOfPrecipiation.text = mutableList[0].pop
    }

    private fun setHourlyData(hourlyList: MutableList<String>, dayTemp: ArrayList<Float>) {
        if (hourlyList.size >= 1) {
            binding.root.hour1.text = hourlyList[0]
            binding.root.hour1.visibility = View.VISIBLE
            binding.root.temp1.text = dayTemp[0].toInt().toString() + "°"
            binding.root.temp1.visibility = View.VISIBLE
            binding.root.hourIcon1.visibility = View.VISIBLE
        }
        if (hourlyList.size >= 2) {
            binding.root.hour2.text = hourlyList[1]
            binding.root.hour2.visibility = View.VISIBLE
            binding.root.temp2.text = dayTemp[1].toInt().toString() + "°"
            binding.root.temp2.visibility = View.VISIBLE
            binding.root.hourIcon2.visibility = View.VISIBLE
        }
        if (hourlyList.size >= 3) {
            binding.root.hour3.text = hourlyList[2]
            binding.root.hour3.visibility = View.VISIBLE
            binding.root.temp3.text = dayTemp[2].toInt().toString() + "°"
            binding.root.temp3.visibility = View.VISIBLE
            binding.root.hourIcon3.visibility = View.VISIBLE
        }
    }

    private fun initDayTitle(map: MutableMap<String, MutableList<WeatherItem>>) {
        for (entry in map.entries) {
            Log.d("WeatherAct", "bindWeatherData:" + entry.key)
            if (!isDay1Present) {
                isDay1Present = true
                binding.root.day1.text = entry.key

                dayWiseData(map[entry.key]!!.toList())
            } else if (!isDay2Present) {
                isDay2Present = true
                binding.root.day2.text = entry.key
            } else if (!isDay3Present) {
                isDay3Present = true
                binding.root.day3.text = entry.key
            } else if (!isDay4Present) {
                isDay4Present = true
                binding.root.day4.text = entry.key
            } else if (!isDay5Present) {
                isDay5Present = true
                binding.root.day5.text = entry.key
            } else if (!isDay6Present) {
                isDay6Present = true
                binding.root.day6.text = entry.key
            }
        }
    }

    private fun mappingDayWiseWeatherData(
        list: List<WeatherItem>,
        map: MutableMap<String, MutableList<WeatherItem>>
    ) {
        for (item in list) {

            val formattedDate = convertDateFormat("yyyy-MM-dd hh:mm:ss", item.dtTxt, "E dd/MM")

            if (map[formattedDate].isNullOrEmpty()) {
                map[formattedDate] = mutableListOf(item)
            } else {
                val oldlist = map[formattedDate]!!
                oldlist.add(item)
                map[formattedDate] = oldlist
            }
        }
    }

    private fun mappingTimeWiseWeatherData(weatherlist: List<WeatherItem>, hourlyList: MutableList<String>) {
        for (item in weatherlist) {
            val formattedDate = convertDateFormat("yyyy-MM-dd hh:mm:ss", item.dtTxt, "hh:mm")
            hourlyList.add(formattedDate)
        }
    }

    private fun handleWeathersList(status: Resource<Weathers>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindWeatherData(weathers = it) }
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
        initChartMax()
        initCharMin()
    }

    private fun initChartMax() {
        // background color
        binding.chartMax.setBackgroundColor(Color.parseColor("#5A91C3"))
        // disable description text
        binding.chartMax.description.isEnabled = false
        // enable touch gestures
        binding.chartMax.setTouchEnabled(false)
        // set listeners
        binding.chartMax.setDrawGridBackground(false)
        // disable scaling and dragging
        binding.chartMax.isDragEnabled = true
        binding.chartMax.setScaleEnabled(false)
        // Disabling force pinch zoom along both axis
        binding.chartMax.setPinchZoom(false)

        binding.chartMax.setDrawGridBackground(false)

        binding.chartMax.axisRight.isEnabled = false
        binding.chartMax.axisRight.setDrawAxisLine(false)
        binding.chartMax.axisRight.setDrawGridLines(false)

        binding.chartMax.axisLeft.isEnabled = false
        binding.chartMax.axisLeft.setDrawAxisLine(false)
        binding.chartMax.axisLeft.setDrawGridLines(false)

        binding.chartMax.xAxis.setDrawAxisLine(false)
        binding.chartMax.xAxis.setDrawGridLines(false)

        binding.chartMax.axisLeft.setDrawAxisLine(false)

        binding.chartMax.legend.isEnabled = false
    }

    private fun initCharMin() {
        // background color
        binding.chartMin.setBackgroundColor(Color.parseColor("#5A91C3"))
        // disable description text
        binding.chartMin.description.isEnabled = false
        // enable touch gestures
        binding.chartMin.setTouchEnabled(false)
        // set listeners
        binding.chartMin.setDrawGridBackground(false)
        // disable scaling and dragging
        binding.chartMin.isDragEnabled = true
        binding.chartMin.setScaleEnabled(false)
        // Disabling force pinch zoom along both axis
        binding.chartMin.setPinchZoom(false)

        binding.chartMin.setDrawGridBackground(false)

        binding.chartMin.axisRight.isEnabled = false
        binding.chartMin.axisRight.setDrawAxisLine(false)
        binding.chartMin.axisRight.setDrawGridLines(false)

        binding.chartMin.axisLeft.isEnabled = false
        binding.chartMin.axisLeft.setDrawAxisLine(false)
        binding.chartMin.axisLeft.setDrawGridLines(false)

        binding.chartMin.xAxis.setDrawAxisLine(false)
        binding.chartMin.xAxis.setDrawGridLines(false)

        binding.chartMin.legend.isEnabled = false
    }

    private fun setMaxData(values: ArrayList<Entry>) {
        val set: LineDataSet
        if (binding.chartMax.data != null && binding.chartMax.data.dataSetCount > 0) {
            set = binding.chartMax.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            set.notifyDataSetChanged()
            binding.chartMax.data.notifyDataChanged()
            binding.chartMax.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set = LineDataSet(values, "DataSet")
            set.setDrawIcons(false)

            // black lines and points
            set.color = Color.WHITE
            set.setCircleColor(Color.WHITE)

            // line thickness and point size
            set.lineWidth = 1f
            set.circleRadius = 3f

            // draw points as solid circles
            set.setDrawCircleHole(false)

            // text size of values
            set.valueTextSize = 9f

            // set the filled area
            set.setDrawFilled(false)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)
            // set data
            binding.chartMax.data = data
        }
    }

    private fun setMinData(values: ArrayList<Entry>) {
        val set: LineDataSet
        if (binding.chartMin.data != null && binding.chartMin.data.dataSetCount > 0) {
            set = binding.chartMin.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            set.notifyDataSetChanged()
            binding.chartMin.data.notifyDataChanged()
            binding.chartMin.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set = LineDataSet(values, "DataSet")
            set.setDrawIcons(false)

            // black lines and points
            set.color = Color.WHITE
            set.setCircleColor(Color.WHITE)

            // line thickness and point size
            set.lineWidth = 1f
            set.circleRadius = 3f

            // draw points as solid circles
            set.setDrawCircleHole(false)

            // text size of values
            set.valueTextSize = 9f

            // set the filled area
            set.setDrawFilled(false)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)
            // set data
            binding.chartMin.data = data
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 100 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(100000).setFastestInterval(100000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        weathersListViewModel.getWeathers(location.latitude, location.longitude)
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
}