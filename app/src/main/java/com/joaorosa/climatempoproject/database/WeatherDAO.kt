package com.joaorosa.climatempoproject.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.joaorosa.climatempoproject.model.WeatherDays
import com.joaorosa.climatempoproject.model.WeatherCity

class WeatherDAO(context: Context) : IWeatherDAO {

    private val write = DatabaseHelper(context).writableDatabase
    private val read = DatabaseHelper(context).readableDatabase

    override fun saveCity(weatherCity: WeatherCity): Long {

        val valores = ContentValues()
        valores.put(DatabaseHelper.COLUMN_CITY, weatherCity.cityName)

        return try {
            write.insert(
                DatabaseHelper.TABLE_NAME_WEATHER_CITY,
                null,
                valores
            )
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override fun saveWeather(weatherDay: WeatherDays): Boolean {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_ID_CITY_TWO_DAYS, weatherDay.cityId)
        values.put(DatabaseHelper.COLUMN_DATE, weatherDay.date)
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, weatherDay.description)
        values.put(DatabaseHelper.COLUMN_MAX, weatherDay.max)
        values.put(DatabaseHelper.COLUMN_MIN, weatherDay.min)

        return try {
            write.insert(
                DatabaseHelper.TABLE_NAME_WEATHER_TWO_DAYS,
                null,
                values
            )
            Log.i("info_db", "Previsão salva com sucesso")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun toList(): List<WeatherCity> {
        val list = mutableListOf<WeatherCity>()

        val sql = """
            SELECT 
                c.${DatabaseHelper.COLUMN_ID_CITY},
                c.${DatabaseHelper.COLUMN_CITY},
                d.${DatabaseHelper.COLUMN_ID},
                d.${DatabaseHelper.COLUMN_DATE},
                d.${DatabaseHelper.COLUMN_DESCRIPTION},
                d.${DatabaseHelper.COLUMN_MAX},
                d.${DatabaseHelper.COLUMN_MIN}
            FROM ${DatabaseHelper.TABLE_NAME_WEATHER_CITY} c
            LEFT JOIN ${DatabaseHelper.TABLE_NAME_WEATHER_TWO_DAYS} d
            ON c.${DatabaseHelper.COLUMN_ID_CITY} = d.${DatabaseHelper.COLUMN_ID_CITY_TWO_DAYS}
        """.trimIndent()

        val cursor = read.rawQuery(sql, null)

        val mapa = mutableMapOf<Int, WeatherCity>()


        val idxCityId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_CITY)
        val idxCityName = cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY)
        val idxId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val idxDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)
        val idxDesc = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
        val idxMax = cursor.getColumnIndex(DatabaseHelper.COLUMN_MAX)
        val idxMin = cursor.getColumnIndex(DatabaseHelper.COLUMN_MIN)

        while (cursor.moveToNext()) {

            val cityId = cursor.getInt(idxCityId)

            val cityName = cursor.getString(idxCityName)

            if (!mapa.containsKey(cityId)) {
                mapa[cityId] = WeatherCity(
                    id = cityId,
                    cityName = cityName,
                    forecastDays = mutableListOf()
                )
            }

            if (idxId != -1 && !cursor.isNull(idxId)) {

                val weatherDay = WeatherDays(
                    id = cursor.getInt(idxId),
                    cityId = cityId,
                    date = cursor.getString(idxDate),
                    description = cursor.getString(idxDesc),
                    max = cursor.getString(idxMax),
                    min = cursor.getString(idxMin)
                )

                mapa[cityId]?.forecastDays?.add(weatherDay)
            }
        }

        cursor.close()

        list.addAll(mapa.values)

        return list
    }

    override fun deleteWeatherDay(id: Int): Boolean {

        val args = arrayOf(id.toString())

        return try {
            write.delete(
                DatabaseHelper.TABLE_NAME_WEATHER_TWO_DAYS,
                "${DatabaseHelper.COLUMN_ID} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao remover WeatherDays id=$id")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao remover WeatherDays id=$id: ${e.message}")
            false
        }
    }

    override fun updateCityName(cityId: Int, newCityName: String): Boolean {

        val args = arrayOf(cityId.toString())

        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_CITY, newCityName)

        return try {
            write.update(
                DatabaseHelper.TABLE_NAME_WEATHER_CITY,
                values,
                "${DatabaseHelper.COLUMN_ID_CITY} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao atualizar cidade id=$cityId para '$newCityName'")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao atualizar cidade id=$cityId: ${e.message}")
            false
        }
    }
}




