package com.example.weatherapp.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun observeViewModel()
    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}