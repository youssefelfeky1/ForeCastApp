package com.example.forecastapp.model

data class ForeCast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherList>,
    val message: Int
)