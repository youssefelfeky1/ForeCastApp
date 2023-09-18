package com.example.forecastapp.model

data class WeatherList (
    var clouds: Clouds,
    var dt: Int,
    var dt_txt: String,
    var main: Main,
    var pop: Double,
    var rain: Rain,
    var sys: Sys,
    var visibility: Int,
    var weather: ArrayList<Weather> = arrayListOf(),
    var wind: Wind
)