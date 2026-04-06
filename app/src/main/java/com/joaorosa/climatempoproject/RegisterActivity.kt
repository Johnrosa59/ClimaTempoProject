package com.joaorosa.climatempoproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.joaorosa.climatempoproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnSendCadastre.setOnClickListener {
            registerUser()
            registerSex()
        }

    }

    fun registerUser() {
        val emailRegistered = binding.editTextEmailRegister.text.toString()

        if(binding.editTextPasswordRegister.text.toString() == binding.editTextPasswordConfirm.text.toString()){
            val passwordRegistered = binding.editTextPasswordConfirm.text.toString()
    }else{
        showMessage("Senhas não conferem")
        }
}


    fun showMessage(mensagem: String){
        Toast.makeText(
            this, mensagem,
            Toast.LENGTH_LONG)
            .show()
    }

    fun registerSex() {
        val selectedMale = binding.radioBtnMale.isChecked
        val selectedFemale = binding.radioBtnFemale.isChecked
        if(selectedMale){

        }else if (selectedFemale){

        }
    }
}