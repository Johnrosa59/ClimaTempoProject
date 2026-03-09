/*
package com.joaorosa.climatempoproject.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaorosa.climatempoproject.databinding.ItemWeatherBinding
import com.joaorosa.climatempoproject.model.WeatherPlaceResponse

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var list = mutableListOf<WeatherPlaceResponse>()

    fun updateListWeather(list: MutableList<WeatherPlaceResponse>){
        this.list = list
        notifyDataSetChanged()
    }
    inner class WeatherViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(){
            
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

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}*/
