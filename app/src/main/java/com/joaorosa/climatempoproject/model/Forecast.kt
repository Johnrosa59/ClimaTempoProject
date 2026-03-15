package com.joaorosa.climatempoproject.model

data class Forecast(
    val cloudiness: Int,
    val condition: String,
    val date: String,
    val description: String,
    val full_date: String,
    val humidity: Int,
    val max: Int,
    val min: Int,
    val moon_phase: String,
    val rain: Double,
    val rain_probability: Int,
    val sunrise: String,
    val sunset: String,
    val weekday: String,
    val wind_speedy: String
)