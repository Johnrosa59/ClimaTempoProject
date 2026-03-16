package com.joaorosa.climatempoproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.joaorosa.climatempoproject.databinding.ActivityChangeItemBinding

class ChangeItemActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChangeItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}