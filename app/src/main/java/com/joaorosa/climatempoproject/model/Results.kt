package com.joaorosa.climatempoproject.model

data class Results(
    val city: String,
    val city_name: String,
    val cloudiness: Int,
    val condition_code: String,
    val condition_slug: String,
    val cref: String,
    val currently: String,
    val date: String,
    val description: String,
    val forecast: List<Forecast>,
    val humidity: Int,
    val img_id: String,
    val moon_phase: String,
    val rain: Int,
    val sunrise: String,
    val sunset: String,
    val temp: Int,
    val time: String,
    val timezone: String,
    val wind_cardinal: String,
    val wind_direction: Int,
    val wind_speedy: String,
    val woeid: Int
)