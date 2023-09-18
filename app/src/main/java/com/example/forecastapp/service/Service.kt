package com.example.forecastapp.service

import com.example.forecastapp.Utils
import com.example.forecastapp.model.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("forecast?")
    fun getCurrentWeather(
        @Query("lat")
        lat:String,
        @Query("lon")
        lon:String,
        @Query("appid")
        appid:String = Utils.API_KEY

    ): Call<ForeCast>
}