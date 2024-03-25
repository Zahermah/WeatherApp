package com.example.volvoweatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volvoweatherapp.model.WeatherResponse
import com.example.volvoweatherapp.repository.RetrofitInstance.weatherService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {
    private val apiKey = "88c1c20bae2acde89572ecfd2ac27766"
    private val _weatherData = MutableLiveData<List<WeatherResponse>>(emptyList())
    val weatherData: LiveData<List<WeatherResponse>> = _weatherData

    init {
        fetchWeatherForCities(
            listOf(
                "London",
                "Stockholm",
                "Berlin",
                "Gothenburg",
                "Mountain View",
                "New York"
            )
        )
    }

    private fun fetchWeatherForCities(cities: List<String>) {
        viewModelScope.launch {
            val responses = cities.map { city ->
                async { fetchWeather(city) }
            }.awaitAll()
            _weatherData.postValue(responses.filterNotNull())
        }
    }

    private suspend fun fetchWeather(city: String): WeatherResponse? {
        return try {
            val response = weatherService.getCurrentWeather(city, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("WeatherViewModel", "Error fetching weather data for $city")
                null
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Exception caught while fetching weather for $city: ", e)
            null
        }
    }
}