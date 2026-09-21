package com.example.aplicativokotlincaoamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroMoradorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro_morador)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtContato = findViewById<EditText>(R.id.edtContato)
        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val edtConfirmarSenha = findViewById<EditText>(R.id.edtConfirmarSenha)
        val btnCadastrarMorador = findViewById<Button>(R.id.btnCadastrarMorador)

        btnCadastrarMorador.setOnClickListener {

            val nome = edtNome.text.toString().trim()
            val contato = edtContato.text.toString().trim()
            val senha = edtSenha.text.toString()
            val confirmarSenha = edtConfirmarSenha.text.toString()

            if (nome.isEmpty()) {
                edtNome.error = "Digite seu nome"
                return@setOnClickListener
            }

            if (contato.isEmpty()) {
                edtContato.error = "Digite seu e-mail ou telefone"
                return@setOnClickListener
            }

            if (senha.isEmpty()) {
                edtSenha.error = "Crie uma senha"
                return@setOnClickListener
            }

            if (confirmarSenha.isEmpty()) {
                edtConfirmarSenha.error = "Confirme sua senha"
                return@setOnClickListener
            }

            if (senha != confirmarSenha) {
                edtConfirmarSenha.error = "As senhas não são iguais"
                return@setOnClickListener
            }
            val preferencias = getSharedPreferences(
                "morador",
                MODE_PRIVATE
            )

            preferencias.edit()
                .putString("senha", senha)
                .apply()
            Toast.makeText(
                this,
                "Cadastro realizado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
