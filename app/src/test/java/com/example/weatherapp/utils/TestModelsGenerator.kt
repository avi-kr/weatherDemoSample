package com.example.weatherapp.utils

import com.example.weatherapp.data.dto.weathor.WeatherItem
import com.example.weatherapp.data.dto.weathor.Weathers
import com.example.weatherapp.data.remote.moshiFactories.MyKotlinJsonAdapterFactory
import com.example.weatherapp.data.remote.moshiFactories.MyStandardJsonAdapters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File
import java.lang.reflect.Type

/**
 * Created by Abhishek Kumar on 31,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
class TestModelsGenerator {

    private var weathers: Weathers = Weathers(arrayListOf())

    init {
        val moshi = Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .build()
        val type: Type = Types.newParameterizedType(List::class.java, WeatherItem::class.java)
        val adapter: JsonAdapter<List<WeatherItem>> = moshi.adapter(type)
        val jsonString = getJson("WeathersApiResponse.json")
        adapter.fromJson(jsonString)?.let {
            weathers = Weathers(ArrayList(it))
        }
        print("this is $weathers")
    }

    fun generateWeathers(): Weathers {
        return weathers
    }

    fun generateWeathersModelWithEmptyList(): Weathers {
        return Weathers(arrayListOf())
    }

    fun generateWeathersItemModel(): WeatherItem {
        return weathers.list[0]
    }

    fun getStubSearchTitle(): String {
        return weathers.list[0].name
    }

    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */

    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}