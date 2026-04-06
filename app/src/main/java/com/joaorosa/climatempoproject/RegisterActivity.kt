package com.joaorosa.climatempoproject

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joaorosa.climatempoproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val database by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Seta de voltar na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cadastro"

        binding.btnVoltar.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

        binding.btnSendCadastre.setOnClickListener {
            if (binding.editTextName.text.isNotEmpty() &&
                binding.editTextAddress.text.isNotEmpty() &&
                binding.editTextEmailRegister.text.isNotEmpty() &&
                binding.editTextPasswordRegister.text.isNotEmpty() &&
                binding.editTextPasswordConfirm.text.isNotEmpty()
            ) {
                registerUser()
            } else {
                showMessage("Preencha todos os campos")
            }
        }
    }

    fun registerUser() {
        val emailRegistered = binding.editTextEmailRegister.text.toString()
        val nameRegistered = binding.editTextName.text.toString()
        val addressRegistered = binding.editTextAddress.text.toString()
        val sexRegistered = registerSex()

        val password = binding.editTextPasswordRegister.text.toString()
        val confirmPassword = binding.editTextPasswordConfirm.text.toString()

        if (password != confirmPassword) {
            showMessage("Senhas não conferem")
            return
        }

        auth.createUserWithEmailAndPassword(emailRegistered, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    saveUserData(
                        nameRegistered,
                        addressRegistered,
                        sexRegistered
                    )
                } else {
                    showMessage("Erro inesperado ao obter UID do usuário")
                }
            }
            .addOnFailureListener { exception ->
                showMessage("Erro ao cadastrar usuário ${exception.message}")
            }
    }

    fun showMessage(mensagem: String) {
        Toast.makeText(
            this, mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    fun saveUserData(name: String, address: String, sex: String) {
        val idUserLoggedIn = auth.currentUser?.uid

        val data = mapOf(
            "nome" to name,
            "endereco" to address,
            "sexo" to sex
        )

        if (idUserLoggedIn != null) {
            database
                .collection("usuarios")
                .document(idUserLoggedIn)
                .set(data)
                .addOnSuccessListener {
                    showMessage("Sucesso ao salvar dados do usuário")
                    finish()
                }
                .addOnFailureListener {
                    showMessage("Erro ao salvar dados do usuário ${it.message}")
                    println(it.message)
                }
        } else {
            showMessage("Usuário não autenticado ao salvar dados")
        }
    }

    fun registerSex(): String {
        val selectedMale = binding.radioBtnMale.isChecked
        val selectedFemale = binding.radioBtnFemale.isChecked
        return when {
            selectedMale -> "Masculino"
            selectedFemale -> "Feminino"
            else -> "Não informado"
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}