package com.example.weatherapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weatherapp.data.dto.weathor.WeatherCity
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.remote.moshiFactories.MyKotlinJsonAdapterFactory
import com.example.weatherapp.data.remote.moshiFactories.MyStandardJsonAdapters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.InputStream
import java.lang.reflect.Type

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
object TestUtil {

    var dataStatus: DataStatus = DataStatus.Success
    var weathers: Weathers = Weathers(arrayListOf(), WeatherCity(""))
    fun initData(): Weathers {
        val moshi = Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .build()
        val type: Type = Types.newParameterizedType(Weathers::class.java, Weathers::class.java)
        val adapter: JsonAdapter<Weathers> = moshi.adapter(type)
        val jsonString = getJson("WeathersApiResponse.json")
        adapter.fromJson(jsonString)?.let {
            weathers = Weathers(ArrayList(it.list), it.city)
            return weathers
        }
        return Weathers(arrayListOf(), WeatherCity(""))
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}