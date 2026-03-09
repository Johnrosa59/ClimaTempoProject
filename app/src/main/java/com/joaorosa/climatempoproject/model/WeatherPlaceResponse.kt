package com.joaorosa.climatempoproject.model

data class WeatherPlaceResponse(
    val `by`: String,
    val results: Results,
    val valid_key: Boolean
)