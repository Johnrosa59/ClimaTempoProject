package com.joaorosa.climatempoproject.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "WeatherDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "weatherSaved"
        const val COLUMN_ID = "id_weather"
        const val COLUMN_CITY = "city"
        const val COLUMN_DATE = "data_weather"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_MAX = "maximum"
        const val COLUMN_MIN = "minimum"



    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                  "$COLUMN_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                  "$COLUMN_CITY VARCHAR(60)," +
                  "$COLUMN_DATE TEXT," +
                  "$COLUMN_DESCRIPTION VARCHAR(60)," +
                  "$COLUMN_MIN VARCHAR(20)," +
                  "$COLUMN_MAX VARCHAR(20)" +
                  ");"


        try {

        }catch (e: Exception){
            db?.execSQL(sql)
            Log.i("info_db","Sucesso ao criar a tabela")
        }

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

}

