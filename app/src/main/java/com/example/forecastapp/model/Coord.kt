package com.example.example.forecastapp

import com.google.gson.annotations.SerializedName


data class Coord (

  @SerializedName("lat" ) var lat : Int? = null,
  @SerializedName("lon" ) var lon : Int? = null

)