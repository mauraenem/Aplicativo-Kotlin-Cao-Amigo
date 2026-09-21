package com.example.aplicativokotlincaoamigo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroCaoActivity : AppCompatActivity() {

    private lateinit var imgFotoCao: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro_cao)

        val numeroCao = intent.getIntExtra(
            "numero_cao",
            1
        )

        imgFotoCao = findViewById(R.id.imgFotoCao)

        val edtNomeCao = findViewById<EditText>(R.id.edtNomeCao)
        val edtIdadeCao = findViewById<EditText>(R.id.edtIdadeCao)
        val edtRua = findViewById<EditText>(R.id.edtRua)
        val edtBairro = findViewById<EditText>(R.id.edtBairro)
        val edtMunicipio = findViewById<EditText>(R.id.edtMunicipio)

        val btnSalvarCao = findViewById<Button>(R.id.btnSalvarCao)

        // Recupera os dados já cadastrados
        val preferencias = getSharedPreferences(
            "caes",
            MODE_PRIVATE
        )

        edtNomeCao.setText(
            preferencias.getString(
                "cao${numeroCao}_nome",
                ""
            )
        )

        edtIdadeCao.setText(
            preferencias.getString(
                "cao${numeroCao}_idade",
                ""
            )
        )

        edtRua.setText(
            preferencias.getString(
                "cao${numeroCao}_rua",
                ""
            )
        )

        edtBairro.setText(
            preferencias.getString(
                "cao${numeroCao}_bairro",
                ""
            )
        )

        edtMunicipio.setText(
            preferencias.getString(
                "cao${numeroCao}_municipio",
                ""
            )
        )

        // Recupera a foto, se existir
        val fotoCao = preferencias.getString(
            "cao${numeroCao}_foto",
            null
        )

        if (fotoCao != null) {

            val uriFoto = Uri.parse(fotoCao)

            imgFotoCao.setImageURI(uriFoto)
        }

        // Escolher outra foto
        imgFotoCao.setOnClickListener {
            abrirGaleria()
        }

        // Salvar alterações
        btnSalvarCao.setOnClickListener {

            val nome = edtNomeCao.text.toString().trim()
            val idade = edtIdadeCao.text.toString().trim()
            val rua = edtRua.text.toString().trim()
            val bairro = edtBairro.text.toString().trim()
            val municipio = edtMunicipio.text.toString().trim()

            if (nome.isEmpty()) {
                edtNomeCao.error = "Digite o nome do cão"
                return@setOnClickListener
            }

            if (idade.isEmpty()) {
                edtIdadeCao.error = "Digite a idade do cão"
                return@setOnClickListener
            }

            if (rua.isEmpty()) {
                edtRua.error = "Digite a rua"
                return@setOnClickListener
            }

            if (bairro.isEmpty()) {
                edtBairro.error = "Digite o bairro"
                return@setOnClickListener
            }

            if (municipio.isEmpty()) {
                edtMunicipio.error = "Digite o município"
                return@setOnClickListener
            }

            preferencias.edit()
                .putString(
                    "cao${numeroCao}_nome",
                    nome
                )
                .putString(
                    "cao${numeroCao}_idade",
                    idade
                )
                .putString(
                    "cao${numeroCao}_rua",
                    rua
                )
                .putString(
                    "cao${numeroCao}_bairro",
                    bairro
                )
                .putString(
                    "cao${numeroCao}_municipio",
                    municipio
                )
                .apply()
            val quantidadeAtual =
                preferencias.getInt(
                    "quantidade_caes",
                    0
                )

            if (numeroCao > quantidadeAtual) {

                preferencias.edit()
                    .putInt(
                        "quantidade_caes",
                        numeroCao
                    )
                    .apply()
            }

            Toast.makeText(
                this,
                "Dados do cão salvos!",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun abrirGaleria() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        intent.type = "image/*"

        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(
            intent,
            100
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == 100 &&
            resultCode == Activity.RESULT_OK
        ) {

            val imagemSelecionada: Uri? = data?.data

            if (imagemSelecionada != null) {

                imgFotoCao.setImageURI(
                    imagemSelecionada
                )

                val numeroCao = intent.getIntExtra(
                    "numero_cao",
                    1
                )

                val preferencias = getSharedPreferences(
                    "caes",
                    MODE_PRIVATE
                )

                preferencias.edit()
                    .putString(
                        "cao${numeroCao}_foto",
                        imagemSelecionada.toString()
                    )
                    .apply()
            }
        }
    }
}