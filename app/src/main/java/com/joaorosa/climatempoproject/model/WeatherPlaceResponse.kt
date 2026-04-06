package com.joaorosa.climatempoproject.model

data class WeatherPlaceResponse(
    val `by`: Any,
    val execution_time: Int,
    val from_cache: Boolean,
    val results: Results,
    val valid_key: Boolean
)