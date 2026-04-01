package com.joaorosa.climatempoproject.database

import com.joaorosa.climatempoproject.model.WeatherCity
import com.joaorosa.climatempoproject.model.WeatherDays

interface IWeatherDAO {

    /** Salva uma cidade e retorna o id gerado (rowId do SQLite) */
    fun saveCity(city: WeatherCity): Long

    /** Salva um dia de previsão (WeatherDays) */
    fun saveWeather(day: WeatherDays): Boolean

    /** Lista todas as cidades com suas previsões (city + forecastDays) */
    fun toList(): List<WeatherCity>

    /** Exclui um WeatherDays pelo id (PK da tabela de dias). */
    fun deleteWeatherDay(id: Int): Boolean

    fun updateCityName(cityId: Int, newCityName: String): Boolean
}