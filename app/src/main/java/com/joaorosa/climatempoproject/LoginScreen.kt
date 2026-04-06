package com.joaorosa.climatempoproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.joaorosa.climatempoproject.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if(binding.editTextEmail.text.isNotEmpty() || binding.editTextPassword.text.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        showMessage("Erro ao fazer login")
                    }
            }else{
                showMessage("Preencha todos os campos")
            }

        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}