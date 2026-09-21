package com.example.aplicativokotlincaoamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BoasVindasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boas_vindas)

        // Encontra o botão no XML
        val btnIrParaHome = findViewById<Button>(R.id.btnIrParaHome)

        // Define a ação do clique para abrir a Home Activity
        btnIrParaHome.setOnClickListener {
            // Cria a intenção de ir para a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // Opcional: fecha a tela de boas-vindas para que, se ele apertar
            // o botão "Voltar" do celular na Home, ele não caia aqui de novo
            finish()
        }
    }
}