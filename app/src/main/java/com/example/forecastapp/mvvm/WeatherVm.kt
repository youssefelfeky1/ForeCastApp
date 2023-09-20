package com.example.forecastapp.mvvm

import android.os.Build
import android.util.Log
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
            val weatherList = response.body()?.list
            cityName.postValue(response.body()?.city!!.name )

            val presentDate = currentDatePattern

            weatherList?.forEach{weather ->
                if(weather.dtTxt!!.split("\\s".toRegex()).contains(presentDate))
                {
                    todayWeatherList.add(weather)
                }
            }

            val closeWeather = findClosestWeather(todayWeatherList)
            closetorexactlysameweatherdata.postValue(closeWeather)

            todayWeatherLiveData.postValue(todayWeatherList)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecastUpcoming(city: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        val forecastWeatherList = mutableListOf<WeatherList>()

        val currentDateTime = LocalDateTime.now()
        val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val sharedPrefs = SharedPrefs(context)
        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()


        val call = if (city != null) {
            RetrofitInstance.api.getWeatherByCity(city)
        } else {
            RetrofitInstance.api.getCurrentWeather(lat, lon)
        }

        val response = call.execute()

        if (response.isSuccessful) {
            val weatherList = response.body()?.list

            val currentDate = currentDateO

            weatherList?.forEach { weather ->

                if (!weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
                    if (weather.dtTxt!!.substring(11, 16) == "12:00") {
                        forecastWeatherList.add(weather)


                    }
                }
            }

            forecastWeatherLiveData.postValue(forecastWeatherList)



            Log.d("Forecast LiveData", forecastWeatherLiveData.value.toString())
        } else {
            val errorMessage = response.message()
            Log.e("CurrentWeatherError", "Error: $errorMessage")
        }
    }






    @RequiresApi(Build.VERSION_CODES.O)
    private fun findClosestWeather(weatherList: List<WeatherList>): WeatherList? {
        val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather: WeatherList? = null
        var minTimeDifference = Int.MAX_VALUE

        for (weather in weatherList) {
            val weatherTime = weather.dtTxt!!.substring(11, 16)
            val timeDifference = Math.abs(timeToMinutes(weatherTime) - timeToMinutes(systemTime))

            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
                closestWeather = weather
            }
        }

        return closestWeather
    }

    private fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }


}