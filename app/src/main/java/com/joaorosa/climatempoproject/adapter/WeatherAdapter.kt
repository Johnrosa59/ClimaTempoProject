package com.joaorosa.climatempoproject.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaorosa.climatempoproject.databinding.ItemWeatherBinding
import com.joaorosa.climatempoproject.model.Forecast

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var listWeather = emptyList<Forecast>()

    fun getWeather(novaLista: List<Forecast>){
        this.listWeather = novaLista
        notifyDataSetChanged()
    }

    var cityTaken = ""

    fun getCity(city: String){
        this.cityTaken = city
        notifyDataSetChanged()
    }
    inner class WeatherViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(weather: Forecast){
            binding.tvData.text = weather.date
            binding.tvWeather.text = weather.description
            binding.txtMax.text = weather.max.toString() + "ºC"
            binding.txtMin.text = weather.min.toString() + "ºC"
            binding.txvCity.text = cityTaken
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherAdapter.WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBinding.inflate(layoutInflater, parent, false)

        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherAdapter.WeatherViewHolder, position: Int) {
        val weather = listWeather[position]
        holder.bind(weather)
    }

    override fun getItemCount(): Int {
        return listWeather.size

    }
}