package com.joaorosa.climatempoproject.api

import com.joaorosa.climatempoproject.model.WeatherPlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getWeather(
        @Query("city_name") cityName: String,
        @Query("key") key: String
    ): Response<WeatherPlaceResponse>
}