package com.example.aplicativokotlincaoamigo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class RegistroCuidadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registro_cuidado)

        // =========================
        // COMPONENTES DA TELA
        // =========================

        val spinnerCao =
            findViewById<Spinner>(R.id.spinnerCao)

        val checkAlimentacao =
            findViewById<CheckBox>(R.id.checkAlimentacao)

        val checkAgua =
            findViewById<CheckBox>(R.id.checkAgua)

        val checkRemedio =
            findViewById<CheckBox>(R.id.checkRemedio)

        val checkVacina =
            findViewById<CheckBox>(R.id.checkVacina)

        val checkCama =
            findViewById<CheckBox>(R.id.checkCama)

        val checkCobertor =
            findViewById<CheckBox>(R.id.checkCobertor)

        val checkOutros =
            findViewById<CheckBox>(R.id.checkOutros)

        val edtOutros =
            findViewById<EditText>(R.id.edtOutros)

        val edtData =
            findViewById<EditText>(R.id.edtData)

        val edtHora =
            findViewById<EditText>(R.id.edtHora)

        val btnSalvar =
            findViewById<Button>(R.id.btnSalvarCuidado)


        // =========================
        // CARREGAR TODOS OS CÃES
        // =========================

        val preferenciasCaes = getSharedPreferences(
            "caes",
            MODE_PRIVATE
        )

        val nomesCaes = mutableListOf<String>()

        val quantidadeCaes = preferenciasCaes.getInt(
            "quantidade_caes",
            0
        )

        for (numeroCao in 1..quantidadeCaes) {

            val nomeCao = preferenciasCaes.getString(
                "cao${numeroCao}_nome",
                ""
            )

            if (!nomeCao.isNullOrEmpty()) {
                nomesCaes.add(nomeCao)
            }
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nomesCaes
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerCao.adapter = adapter


        // =========================
        // SELETOR DE DATA
        // =========================

        edtData.setOnClickListener {

            val calendario = Calendar.getInstance()

            val seletorData = DatePickerDialog(
                this,
                { _, ano, mes, dia ->

                    val dataFormatada = String.format(
                        "%02d/%02d/%04d",
                        dia,
                        mes + 1,
                        ano
                    )

                    edtData.setText(dataFormatada)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )

            seletorData.show()
        }


        // =========================
        // SELETOR DE HORA
        // =========================

        edtHora.setOnClickListener {

            val calendario = Calendar.getInstance()

            val seletorHora = TimePickerDialog(
                this,
                { _, hora, minuto ->

                    val horaFormatada = String.format(
                        "%02d:%02d",
                        hora,
                        minuto
                    )

                    edtHora.setText(horaFormatada)
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            )

            seletorHora.show()
        }


        // =========================
        // BOTÃO SALVAR
        // =========================

        btnSalvar.setOnClickListener {

            if (spinnerCao.selectedItem == null) {

                Toast.makeText(
                    this,
                    "Cadastre pelo menos um cão.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val caoSelecionado =
                spinnerCao.selectedItem.toString()

            val data =
                edtData.text.toString().trim()

            val hora =
                edtHora.text.toString().trim()


            // =========================
            // VALIDAÇÕES
            // =========================

            if (data.isEmpty()) {

                edtData.error = "Informe a data"

                return@setOnClickListener
            }

            if (hora.isEmpty()) {

                edtHora.error = "Informe a hora"

                return@setOnClickListener
            }


            // =========================
            // RECUPERAR HISTÓRICO
            // =========================

            val preferencias = getSharedPreferences(
                "registros_cuidado",
                MODE_PRIVATE
            )

            val registros = JSONArray(
                preferencias.getString(
                    "lista",
                    "[]"
                )
            )


            // =========================
            // CRIAR NOVO REGISTRO
            // =========================

            val registro = JSONObject()

            registro.put(
                "cao",
                caoSelecionado
            )

            registro.put(
                "alimentacao",
                checkAlimentacao.isChecked
            )

            registro.put(
                "agua",
                checkAgua.isChecked
            )

            registro.put(
                "remedio",
                checkRemedio.isChecked
            )

            registro.put(
                "vacina",
                checkVacina.isChecked
            )

            registro.put(
                "cama",
                checkCama.isChecked
            )

            registro.put(
                "cobertor",
                checkCobertor.isChecked
            )

            registro.put(
                "outros",
                checkOutros.isChecked
            )

            registro.put(
                "descricao_outros",
                edtOutros.text.toString().trim()
            )

            registro.put(
                "data",
                data
            )

            registro.put(
                "hora",
                hora
            )


            // =========================
            // ADICIONAR AO HISTÓRICO
            // =========================

            registros.put(registro)


            // =========================
            // SALVAR HISTÓRICO
            // =========================

            preferencias.edit()
                .putString(
                    "lista",
                    registros.toString()
                )
                .apply()


            // =========================
            // CONFIRMAÇÃO
            // =========================

            Toast.makeText(
                this,
                "Registro de cuidado salvo!",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}