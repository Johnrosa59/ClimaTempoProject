package com.joaorosa.climatempoproject.database

import com.joaorosa.climatempoproject.model.WeatherCity
import com.joaorosa.climatempoproject.model.WeatherDays

interface IWeatherDAO {

    fun saveCity(city: WeatherCity): Long

    fun saveWeather(day: WeatherDays): Boolean

    fun toList(): List<WeatherCity>

    fun deleteWeatherDay(id: Int): Boolean

    fun updateCityName(cityId: Int, newCityName: String): Boolean
}