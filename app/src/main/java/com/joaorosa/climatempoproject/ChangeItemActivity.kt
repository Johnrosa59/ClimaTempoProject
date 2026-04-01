package com.joaorosa.climatempoproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joaorosa.climatempoproject.api.RetrofitService
import com.joaorosa.climatempoproject.database.WeatherDAO
import com.joaorosa.climatempoproject.databinding.ActivityChangeItemBinding
import com.joaorosa.climatempoproject.model.WeatherCity
import com.joaorosa.climatempoproject.model.WeatherDays
import com.joaorosa.climatempoproject.model.WeatherPlaceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ChangeItemActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChangeItemBinding.inflate(layoutInflater)
    }

    private lateinit var dao: WeatherDAO

    private var cityId: Int = -1

    // Mesmo mapa da MainActivity
    private val state = mapOf(
        "Acre" to "AC",
        "Alagoas" to "AL",
        "Amapá" to "AP",
        "Amazonas" to "AM",
        "Bahia" to "BA",
        "Ceará" to "CE",
        "Distrito Federal" to "DF",
        "Espírito Santo" to "ES",
        "Goiás" to "GO",
        "Maranhão" to "MA",
        "Mato Grosso" to "MT",
        "Mato Grosso do Sul" to "MS",
        "Minas Gerais" to "MG",
        "Pará" to "PA",
        "Paraíba" to "PB",
        "Paraná" to "PR",
        "Pernambuco" to "PE",
        "Piauí" to "PI",
        "Rio de Janeiro" to "RJ",
        "Rio Grande do Norte" to "RN",
        "Rio Grande do Sul" to "RS",
        "Rondônia" to "RO",
        "Roraima" to "RR",
        "Santa Catarina" to "SC",
        "São Paulo" to "SP",
        "Sergipe" to "SE",
        "Tocantins" to "TO"
    )

    private val weatherAPI by lazy {
        RetrofitService.weatherAPI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dao = WeatherDAO(this)

        binding.btnChangeSave.setOnClickListener {
            val texto = binding.editWeathertoChange.text.toString().trim()

            if (texto.isBlank()) {
                Toast.makeText(this, "Digite Cidade, Estado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeCityWithAPI(texto)
        }
    }
    private fun changeCityWithAPI(cityState: String) {

        val parts = cityState.split(",")
        if (parts.size < 2) {
            Toast.makeText(
                this,
                "Digite no formato: Cidade, Estado (ex: São Paulo, SP)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val city = parts[0].trim()
        val state = parts[1].trim()

        val stateAcronym = this@ChangeItemActivity.state[state]
        if (stateAcronym == null) {
            Toast.makeText(this, "Estado inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val transformedValueCityState = "$city,$stateAcronym"

        CoroutineScope(Dispatchers.IO).launch {

            var response: Response<WeatherPlaceResponse>? = null

            try {
                response = weatherAPI.getWeather(
                    transformedValueCityState,
                    RetrofitService.API_KEY
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChangeItemActivity,
                        "Erro ao fazer a requisição",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            if (response != null && response.isSuccessful) {
                val responseWeather = response.body()

                if (responseWeather != null) {

                    // 1) Salvar nova cidade
                    val idCidade = dao.saveCity(
                        WeatherCity(
                            id = -1,
                            cityName = responseWeather.results.city,
                            forecastDays = mutableListOf()
                        )
                    )

                    if (idCidade == -1L) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@ChangeItemActivity,
                                "Erro ao salvar cidade no banco.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        return@launch
                    }

                    // 2) Salvar previsões dessa nova cidade
                    responseWeather.results.forecast.forEach { forecast ->
                        dao.saveWeather(
                            WeatherDays(
                                id = 0,
                                cityId = idCidade.toInt(),
                                date = forecast.date,
                                description = forecast.description,
                                max = forecast.max.toString(),
                                min = forecast.min.toString()
                            )
                        )
                    }

                    // 3) Finaliza a tela e deixa a MainActivity recarregar os dados
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChangeItemActivity,
                            "Cidade alterada com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChangeItemActivity,
                            "Não foi possível recuperar o clima.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else if (response != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChangeItemActivity,
                        "Erro na requisição. Código: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}