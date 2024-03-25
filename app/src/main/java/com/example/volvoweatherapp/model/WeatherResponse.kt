package com.example.volvoweatherapp.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    val wind: Wind,
    val sys: Sys
)

data class Main(val temp: Double, val humidity: Int)

data class Weather(val description: String, val id: Int)

data class Wind(
    val speed: Double, // Wind speed
    val deg: Int // Wind direction in degrees
)

data class Sys(
    val sunrise: Long, // Sunrise time in Unix timestamp
    val sunset: Long // Sunset time in Unix timestamp
)