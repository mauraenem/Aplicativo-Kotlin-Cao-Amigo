package com.example.aplicativokotlincaoamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val btnListaCaes =
            findViewById<Button>(R.id.btnListaCaes)

        val btnRegistroCuidado =
            findViewById<Button>(R.id.btnRegistroCuidado)

        val btnHistoricoCuidado =
            findViewById<Button>(R.id.btnHistoricoCuidado)

        btnListaCaes.setOnClickListener {

            val intent = Intent(
                this,
                ListaCaesActivity::class.java
            )

            startActivity(intent)
        }

        btnRegistroCuidado.setOnClickListener {

            val intent = Intent(
                this,
                RegistroCuidadoActivity::class.java
            )

            startActivity(intent)
        }

        btnHistoricoCuidado.setOnClickListener {

            val intent = Intent(
                this,
                HistoricoCuidadoActivity::class.java
            )

            startActivity(intent)
        }
    }
}