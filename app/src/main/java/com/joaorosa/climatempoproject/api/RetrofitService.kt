package com.joaorosa.climatempoproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    const val API_KEY = "23b69810"
    const val BASE_URL = "https://api.hgbrasil.com/"

    val weatherAPI = Retrofit.Builder()
        .baseUrl(RetrofitService.BASE_URL)
        .addConverterFactory( GsonConverterFactory.create() )
        .build()
        .create(WeatherAPI::class.java)

}