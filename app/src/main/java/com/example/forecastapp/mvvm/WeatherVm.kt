package com.example.forecastapp.mvvm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecastapp.MyApplication
import com.example.forecastapp.SharedPrefs
import com.example.forecastapp.modal.WeatherList
import com.example.forecastapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm:ViewModel() {
    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()

    val closetorexactlysameweatherdata = MutableLiveData<WeatherList?>()
    val cityName = MutableLiveData<String>()
    val context = MyApplication.instance

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(city:String?=null)= viewModelScope.launch(Dispatchers.IO) {

        val todayWeatherList = mutableListOf<WeatherList>()
        val currentDateTime = LocalDateTime.now()
        val currentDatePattern = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val sharedPrefs = SharedPrefs(context)

        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        val call = if(city!=null)
        {
            RetrofitInstance.api.getWeatherByCity(city)
        }
        else{
            RetrofitInstance.api.getWeatherByCity(lat,lon)
        }

        val response = call.execute()

        if(response.isSuccessful)
        {
            val weatherList = response.body()?.weatherList
            cityName.postValue(response.body()?.city!!.name )

            val presentDate = currentDatePattern

            weatherList?.forEach{weather ->
                if(weather.dtTxt!!.split("\\s".toRegex()).contains(presentDate))
                {
                    todayWeatherList.add(weather)
                }
            }
        }

        val closeWeather = findCloseWeather(todayWeatherList)
        closetorexactlysameweatherdata.postValue(closeWeather)

        todayWeatherLiveData.postValue(todayWeatherList)
    }

}