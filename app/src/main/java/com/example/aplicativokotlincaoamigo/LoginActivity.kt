package com.example.aplicativokotlincaoamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val edtSenhaLogin = findViewById<EditText>(R.id.edtSenhaLogin)
        val btnEntrarLogin = findViewById<Button>(R.id.btnEntrarLogin)

        btnEntrarLogin.setOnClickListener {

            val senhaDigitada = edtSenhaLogin.text.toString()

            if (senhaDigitada.isEmpty()) {
                edtSenhaLogin.error = "Digite sua senha"
                return@setOnClickListener
            }

            val preferencias = getSharedPreferences(
                "morador",
                MODE_PRIVATE
            )

            val senhaSalva = preferencias.getString(
                "senha",
                null
            )

            if (senhaSalva == null) {

                Toast.makeText(
                    this,
                    "Nenhum morador cadastrado.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (senhaDigitada == senhaSalva) {

                Toast.makeText(
                    this,
                    "Login realizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    HomeActivity::class.java
                )

                startActivity(intent)

                finish()

            } else {

                edtSenhaLogin.error = "Senha incorreta"
            }
        }
    }
}