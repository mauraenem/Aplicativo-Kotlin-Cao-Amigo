package com.example.aplicativokotlincaoamigo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListaCaesActivity : AppCompatActivity() {

    private var paginaAtual = 0

    private val caesPorPagina = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lista_caes)

        val imgCaes = listOf<ImageButton>(
            findViewById(R.id.imgCao1),
            findViewById(R.id.imgCao2),
            findViewById(R.id.imgCao3),
            findViewById(R.id.imgCao4),
            findViewById(R.id.imgCao5)
        )

        val btnNovoCao =
            findViewById<Button>(R.id.btnNovoCao)

        val btnAnterior =
            findViewById<Button>(R.id.btnPaginaAnterior)

        val btnProxima =
            findViewById<Button>(R.id.btnProximaPagina)

        // Cada posição da tela abre o cão correspondente
        imgCaes.forEachIndexed { indice, imagem ->

            imagem.setOnClickListener {

                val numeroCao =
                    paginaAtual * caesPorPagina + indice + 1

                val quantidade = obterQuantidadeCaes()

                if (numeroCao <= quantidade) {
                    abrirCadastro(numeroCao)
                }
            }
        }

        btnNovoCao.setOnClickListener {

            val novoNumero =
                obterQuantidadeCaes() + 1

            abrirCadastro(novoNumero)
        }

        btnAnterior.setOnClickListener {

            if (paginaAtual > 0) {

                paginaAtual--

                carregarPagina()
            }
        }

        btnProxima.setOnClickListener {

            val quantidade =
                obterQuantidadeCaes()

            if (
                (paginaAtual + 1) * caesPorPagina
                < quantidade
            ) {

                paginaAtual++

                carregarPagina()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        carregarPagina()
    }

    private fun abrirCadastro(numeroCao: Int) {

        val intent = Intent(
            this,
            CadastroCaoActivity::class.java
        )

        intent.putExtra(
            "numero_cao",
            numeroCao
        )

        startActivity(intent)
    }

    private fun obterQuantidadeCaes(): Int {

        val preferencias = getSharedPreferences(
            "caes",
            MODE_PRIVATE
        )

        var quantidade =
            preferencias.getInt(
                "quantidade_caes",
                0
            )

        // Esta parte reconhece os 5 cães que você
        // já cadastrou antes desta alteração.
        if (quantidade == 0) {

            var ultimoCaoEncontrado = 0

            for (numero in 1..100) {

                val nome = preferencias.getString(
                    "cao${numero}_nome",
                    ""
                )

                if (!nome.isNullOrEmpty()) {
                    ultimoCaoEncontrado = numero
                }
            }

            quantidade = ultimoCaoEncontrado

            preferencias.edit()
                .putInt(
                    "quantidade_caes",
                    quantidade
                )
                .apply()
        }

        return quantidade
    }

    private fun carregarPagina() {

        val preferencias = getSharedPreferences(
            "caes",
            MODE_PRIVATE
        )

        val quantidade =
            obterQuantidadeCaes()

        val imagens = listOf<ImageButton>(
            findViewById(R.id.imgCao1),
            findViewById(R.id.imgCao2),
            findViewById(R.id.imgCao3),
            findViewById(R.id.imgCao4),
            findViewById(R.id.imgCao5)
        )

        val nomes = listOf<TextView>(
            findViewById(R.id.txtCao1),
            findViewById(R.id.txtCao2),
            findViewById(R.id.txtCao3),
            findViewById(R.id.txtCao4),
            findViewById(R.id.txtCao5)
        )

        for (indice in 0 until caesPorPagina) {

            val numeroCao =
                paginaAtual * caesPorPagina + indice + 1

            val imagem = imagens[indice]
            val texto = nomes[indice]

            if (numeroCao <= quantidade) {

                imagem.visibility = View.VISIBLE
                texto.visibility = View.VISIBLE

                val nomeCao =
                    preferencias.getString(
                        "cao${numeroCao}_nome",
                        "Cão $numeroCao"
                    )

                texto.text = nomeCao

                val fotoCao =
                    preferencias.getString(
                        "cao${numeroCao}_foto",
                        null
                    )

                if (fotoCao != null) {

                    try {

                        imagem.setImageURI(
                            Uri.parse(fotoCao)
                        )

                    } catch (e: Exception) {

                        imagem.setImageResource(
                            android.R.drawable.ic_menu_camera
                        )
                    }

                } else {

                    imagem.setImageResource(
                        android.R.drawable.ic_menu_camera
                    )
                }

            } else {

                imagem.visibility = View.GONE
                texto.visibility = View.GONE
            }
        }

        val totalPaginas =
            if (quantidade == 0) {
                1
            } else {
                (quantidade + caesPorPagina - 1) /
                        caesPorPagina
            }

        findViewById<TextView>(
            R.id.txtPagina
        ).text =
            "Página ${paginaAtual + 1} de $totalPaginas"

        findViewById<Button>(
            R.id.btnPaginaAnterior
        ).isEnabled =
            paginaAtual > 0

        findViewById<Button>(
            R.id.btnProximaPagina
        ).isEnabled =
            (paginaAtual + 1) < totalPaginas
    }
}