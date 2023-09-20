package com.example.forecastapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.forecastapp.adapter.WeatherToday
import com.example.forecastapp.databinding.ActivityMainBinding
import com.example.forecastapp.mvvm.WeatherVm

class MainActivity : AppCompatActivity() {

    lateinit var viM: WeatherVm

    lateinit var adapter: WeatherToday

    private lateinit var binding:ActivityMainBinding

    var longi: String = ""
    var lati: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viM = ViewModelProvider(this).get(WeatherVm::class.java)
        adapter= WeatherToday()

        val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()
    }
}