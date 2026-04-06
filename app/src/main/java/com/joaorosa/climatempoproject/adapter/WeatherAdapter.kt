package com.joaorosa.climatempoproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaorosa.climatempoproject.databinding.ItemWeatherBinding
import com.joaorosa.climatempoproject.model.WeatherDays

class WeatherAdapter(
    private val onDeleteClick: (WeatherDays) -> Unit,
    private val onEditClick: (WeatherDays) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var listWeather = mutableListOf<WeatherDays>()
    private var cityTaken: String = ""

    fun setWeatherList(novaLista: List<WeatherDays>) {
        listWeather = novaLista.toMutableList()
        notifyDataSetChanged()
    }

    fun setCity(city: String) {
        cityTaken = city
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weather: WeatherDays) {
            binding.tvDate.text = weather.date
            binding.tvWeather.text = weather.description
            binding.txtMax.text = "${weather.max}ºC"
            binding.txtMin.text = "${weather.min}ºC"
            binding.txvCity.text = cityTaken

            binding.btnDelete.setOnClickListener {
                onDeleteClick(weather)
            }

            binding.btnEdit.setOnClickListener {
                onEditClick(weather)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = listWeather[position]
        holder.bind(weather)
    }

    override fun getItemCount(): Int = listWeather.size

    fun removeItem(weather: WeatherDays) {
        println(listWeather)
        val index = listWeather.indexOf(weather)
        println(index)
        if (index != -1) {
            listWeather.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}