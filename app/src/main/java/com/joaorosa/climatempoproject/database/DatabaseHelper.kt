package com.joaorosa.climatempoproject.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "Clima.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME_WEATHER_CITY = "Clima_Cidade"
        const val TABLE_NAME_WEATHER_TWO_DAYS = "Clima_Dois_Dias"

        const val COLUMN_ID_CITY = "Id_Cidade"
        const val COLUMN_ID_CITY_TWO_DAYS = "Id_Cidade"

        const val COLUMN_CITY = "Nome_Cidade"
        const val COLUMN_DATE = "Data_Clima"
        const val COLUMN_DESCRIPTION = "Descricao"

        const val COLUMN_MAX = "Maxima"
        const val COLUMN_MIN = "Minima"
        const val COLUMN_ID = "Id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val weatherCity = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME_WEATHER_CITY (
                $COLUMN_ID_CITY INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CITY TEXT NOT NULL
            );
        """.trimIndent()

        val weatherTwoDays = """
            CREATE TABLE IF NOT EXISTS  $TABLE_NAME_WEATHER_TWO_DAYS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ID_CITY_TWO_DAYS INTEGER,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_MAX TEXT,
                $COLUMN_MIN TEXT,
                FOREIGN KEY ($COLUMN_ID_CITY_TWO_DAYS) 
                    REFERENCES $TABLE_NAME_WEATHER_CITY($COLUMN_ID_CITY)
                    ON DELETE CASCADE
            );
        """.trimIndent()

        try {
            db?.execSQL(weatherCity)
            db?.execSQL(weatherTwoDays)
            Log.i("info_db", "Sucesso ao criar as tabelas")
        } catch (e: Exception) {
            Log.i("info_db", "Erro ao criar as tabelas ${e.message}")
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Deixar vazio por enquanto
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}