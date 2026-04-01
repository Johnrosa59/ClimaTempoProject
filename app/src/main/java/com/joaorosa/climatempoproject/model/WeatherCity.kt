package com.joaorosa.climatempoproject.model

data class WeatherCity(
    val id: Int,
    val cityName: String,
    val forecastDays: MutableList<WeatherDays>
    )
