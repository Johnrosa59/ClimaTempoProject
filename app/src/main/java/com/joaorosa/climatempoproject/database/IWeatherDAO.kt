package com.joaorosa.climatempoproject.database

import com.joaorosa.climatempoproject.model.WeatherCity
import com.joaorosa.climatempoproject.model.WeatherDays

interface IWeatherDAO {

    fun saveCity(weatherCity: WeatherCity): Long

    fun saveWeather(weatherDay: WeatherDays): Boolean

    fun toList(): List<WeatherCity>

    fun deleteWeatherDay(id: Int): Boolean

    fun updateCityName(cityId: Int, newCityName: String): Boolean
}