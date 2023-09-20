package com.example.forecastapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecastapp.adapter.WeatherToday
import com.example.forecastapp.databinding.ActivityMainBinding
import com.example.forecastapp.modal.WeatherList
import com.example.forecastapp.mvvm.WeatherVm
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viM: WeatherVm

    lateinit var adapter: WeatherToday

    private lateinit var binding:ActivityMainBinding

    var longi: String = ""
    var lati: String = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viM = ViewModelProvider(this).get(WeatherVm::class.java)
        adapter= WeatherToday()

        binding.lifecycleOwner = this
        binding.vm=viM

        val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()



        viM.todayWeatherLiveData.observe(this, Observer {

            val setNewlist = it as List<WeatherList>


            Log.e("TODayweather list", it.toString())
            adapter.setList(setNewlist)
            binding.forecastRecyclerView.adapter = adapter


        })


        viM.closetorexactlysameweatherdata.observe(this, Observer {


            val temperatureFahrenheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)


            for (i in it.weather) {


                binding.descMain.text = i.description


            }

            binding.tempMain.text = "$temperatureFormatted°"


            binding.humidityMain.text = it.main!!.humidity.toString()
            binding.windSpeed.text = it.wind?.speed.toString()


            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(it.dtTxt!!)
            val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
            val dateanddayname = outputFormat.format(date!!)

            binding.dateDayMain.text = dateanddayname

            binding.chanceofrain.text = "${it.pop.toString()}%"


            // setting the icon
            for (i in it.weather) {


                if (i.icon == "01d") {


                    binding.imageMain.setImageResource(R.drawable.oned)

                }

                if (i.icon == "01n") {
                    binding.imageMain.setImageResource(R.drawable.onen)


                }

                if (i.icon == "02d") {

                    binding.imageMain.setImageResource(R.drawable.twod)


                }


                if (i.icon == "02n") {
                    binding.imageMain.setImageResource(R.drawable.twon)


                }


                if (i.icon == "03d" || i.icon == "03n") {


                    binding.imageMain.setImageResource(R.drawable.threedn)


                }



                if (i.icon == "10d") {

                    binding.imageMain.setImageResource(R.drawable.tend)


                }


                if (i.icon == "10n") {

                    binding.imageMain.setImageResource(R.drawable.tenn)


                }


                if (i.icon == "04d" || i.icon == "04n") {


                    binding.imageMain.setImageResource(R.drawable.fourdn)


                }


                if (i.icon == "09d" || i.icon == "09n") {


                    binding.imageMain.setImageResource(R.drawable.ninedn)


                }



                if (i.icon == "11d" || i.icon == "11n") {


                    binding.imageMain.setImageResource(R.drawable.elevend)


                }


                if (i.icon == "13d" || i.icon == "13n") {

                    binding.imageMain.setImageResource(R.drawable.thirteend)


                }

                if (i.icon == "50d" || i.icon == "50n") {


                    binding.imageMain.setImageResource(R.drawable.fiftydn)


                }

            }

        })

        if (checkLocationPermissions())
        {
            getCurrentLocation()
        }
        else{
            requestLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {
      val fineLocationPermission= ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocationPermission= ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return fineLocationPermission==PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions(){

        ActivityCompat.requestPermissions(this,
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),Utils.PERMISSION_REQUEST_CODE)

    }

    private fun getCurrentLocation(){

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==Utils.PERMISSION_REQUEST_CODE){

        }

    }
}