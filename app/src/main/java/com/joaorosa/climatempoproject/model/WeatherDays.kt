package com.joaorosa.climatempoproject.model

data class WeatherDays(
    val id: Int,
    val cityId: Int,
    val date: String,
    val description: String,
    val max: String,
    val min: String
)
