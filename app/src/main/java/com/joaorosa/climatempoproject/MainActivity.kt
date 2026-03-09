package com.joaorosa.climatempoproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
//import com.joaorosa.climatempoproject.adapter.WeatherAdapter
import com.joaorosa.climatempoproject.api.RetrofitService
import com.joaorosa.climatempoproject.databinding.ActivityMainBinding
import com.joaorosa.climatempoproject.model.WeatherPlaceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val weatherAPI by lazy {
        RetrofitService.weatherAPI
    }

    val estadosMap = mapOf(
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

   // private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //adapter = WeatherAdapter()

        //adapter.updateListWeather(list)

        binding.rvWeather.layoutManager =
            LinearLayoutManager(this)


        setContentView(binding.root)
    }

    private fun recoveryWeather() {

        CoroutineScope( Dispatchers.IO ).launch {

            val cityState = "Curitiba,PR"
            var response: Response<WeatherPlaceResponse>? = null

            try {
                response = weatherAPI.getWeather(
                    cityState,
                    RetrofitService.API_KEY
                )
            }catch (e: Exception){
                showMessage("Erro ao fazer a requisição")
            }

            if( response != null ){
                if( response.isSuccessful ){
                    val result = response.body()
                    if(result != null){
                        val results = result.results
                        Log.i("teste", "recuperar clima: $results")
                    }

                }else{
                    showMessage("Não foi possível recuperar o clima recente CODIGO: ${response.code()}")
                }
            }else{
                showMessage("Não foi possível fazer a requisição")
            }

        }
    }
    private fun showMessage(mensagem: String ) {
        Toast.makeText(
            applicationContext,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }
}