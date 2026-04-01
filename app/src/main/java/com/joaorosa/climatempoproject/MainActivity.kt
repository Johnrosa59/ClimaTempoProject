package com.joaorosa.climatempoproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaorosa.climatempoproject.adapter.WeatherAdapter
import com.joaorosa.climatempoproject.api.RetrofitService
import com.joaorosa.climatempoproject.database.WeatherDAO
import com.joaorosa.climatempoproject.databinding.ActivityMainBinding
import com.joaorosa.climatempoproject.model.WeatherCity
import com.joaorosa.climatempoproject.model.WeatherDays
import com.joaorosa.climatempoproject.model.WeatherPlaceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val weatherAPI by lazy {
        RetrofitService.weatherAPI
    }

    private var adapter: WeatherAdapter? = null
    private lateinit var dao: WeatherDAO

    private var currentCityName: String = ""

    private val estados = mapOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dao = WeatherDAO(this)

        // Adapter com callbacks de deletar e editar
        adapter = WeatherAdapter(
            onDeleteClick = { weatherClicked->
                deleteWeatherDay(weatherClicked)
            },
            onEditClick = { weatherClicked ->
                openEditScreen(weatherClicked)
            }
        )

        binding.rvWeather.layoutManager = LinearLayoutManager(this)
        binding.rvWeather.adapter = adapter

        binding.btnSearch.setOnClickListener {
            recoveryWeather()
        }

        // Carregar dados do banco ao abrir o app
        CoroutineScope(Dispatchers.IO).launch {
            val cidades = dao.toList()

            if (cidades.isNotEmpty()) {
                val ultimaCidade = cidades.last()
                withContext(Dispatchers.Main) {
                    currentCityName = ultimaCidade.cityName
                    adapter?.setCity(ultimaCidade.cityName)
                    adapter?.setWeatherList(ultimaCidade.forecastDays)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            val cidades = dao.toList()

            if (cidades.isNotEmpty()) {
                val ultimaCidade = cidades.last()
                withContext(Dispatchers.Main) {
                    currentCityName = ultimaCidade.cityName
                    adapter?.setCity(ultimaCidade.cityName)
                    adapter?.setWeatherList(ultimaCidade.forecastDays)
                }
            }
        }
    }

    fun recoveryWeather() {

        val cityState = binding.editSearch.text.toString()

        // Espera formato: "Cidade, Estado"
        val parts = cityState.split(",")

        if (parts.size < 2) {
            CoroutineScope(Dispatchers.Main).launch {
                showMessage("Digite no formato: Cidade, Estado (ex: São Paulo, SP)")
            }
            return
        }

        val city = parts[0].trim()
        val state = parts[1].trim()

        val estadoSigla = estados[state]

        if (estadoSigla == null) {
            CoroutineScope(Dispatchers.Main).launch {
                showMessage("Estado inválido.")
            }
            return
        }

        val transformedValueCityState = "$city,$estadoSigla"

        CoroutineScope(Dispatchers.IO).launch {

            var response: Response<WeatherPlaceResponse>? = null

            try {
                response = weatherAPI.getWeather(
                    transformedValueCityState,
                    RetrofitService.API_KEY
                )
            } catch (e: Exception) {
                showMessage("Erro ao fazer a requisição")
            }

            if (response != null) {
                if (response.isSuccessful) {
                    val responseWeather = response.body()
                    if (responseWeather != null) {

                        // 1) Salvar cidade
                        val idCidade = dao.saveCity(
                            WeatherCity(
                                id = -1,
                                cityName = responseWeather.results.city,
                                forecastDays = mutableListOf()
                            )
                        )

                        if (idCidade == -1L) {
                            showMessage("Erro ao salvar cidade no banco.")
                            return@launch
                        }

                        responseWeather.results.forecast.forEach { forecast ->
                            dao.saveWeather(
                                WeatherDays(
                                    0,// autoincrement
                                    idCidade.toInt(),
                                    forecast.date,
                                    forecast.description,
                                    forecast.max.toString(),
                                    forecast.min.toString()
                                )
                            )
                        }

                        val cidadesComPrevisao = dao.toList()

                        withContext(Dispatchers.Main) {
                            if (cidadesComPrevisao.isNotEmpty()) {

                                val ultimaCidade = cidadesComPrevisao.last()
                                currentCityName = ultimaCidade.cityName
                                adapter?.setCity(ultimaCidade.cityName)
                                adapter?.setWeatherList(ultimaCidade.forecastDays)

                            } else {
                                showMessage("Nenhum dado encontrado no banco.")
                            }
                        }

                    } else {
                        showMessage("Não foi possível recuperar o clima. CÓDIGO: ${response.code()}")
                    }
                } else {
                    showMessage("Não foi possível fazer a requisição")
                }
            }
        }
    }

    private fun deleteWeatherDay(weather: WeatherDays) {
        val id = weather.id

        CoroutineScope(Dispatchers.IO).launch {
            val apagou = dao.deleteWeatherDay(id)

            withContext(Dispatchers.Main) {
                if (apagou) {
                    adapter?.removeItem(weather)
                    showMessage("Item removido com sucesso.")
                } else {
                    showMessage("Erro ao remover item.")
                }
            }
        }
    }

    private fun openEditScreen(weather: WeatherDays) {
        val intent = Intent(this, ChangeItemActivity::class.java).apply {
        }
        startActivity(intent)
    }

    private suspend fun showMessage(mensagem: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                applicationContext,
                mensagem,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}