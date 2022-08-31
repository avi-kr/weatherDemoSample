package com.example.weatherapp.ui.component.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.weatherapp.SPLASH_DELAY
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.ui.base.BaseActivity
import com.example.weatherapp.ui.component.weather.WeatherActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun observeViewModel() {
    }

    override fun initViewBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    private fun navigateToMainScreen() {
        Handler().postDelayed({
            val nextScreenIntent = Intent(this, WeatherActivity::class.java)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_DELAY.toLong())
    }
}