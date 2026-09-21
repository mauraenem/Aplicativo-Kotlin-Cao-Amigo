package com.example.aplicativokotlincaoamigo

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class HistoricoCuidadoActivity : AppCompatActivity() {

    private lateinit var layoutRegistros: LinearLayout
    private lateinit var spinnerFiltroCao: Spinner

    private var registros = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_historico_cuidado)

        layoutRegistros =
            findViewById(R.id.layoutRegistros)

        spinnerFiltroCao =
            findViewById(R.id.spinnerFiltroCao)

        carregarRegistros()

        configurarFiltro()
    }

    private fun carregarRegistros() {

        val preferencias = getSharedPreferences(
            "registros_cuidado",
            MODE_PRIVATE
        )

        val listaSalva = preferencias.getString(
            "lista",
            "[]"
        )

        registros = JSONArray(listaSalva)
    }

    private fun configurarFiltro() {

        val preferenciasCaes = getSharedPreferences(
            "caes",
            MODE_PRIVATE
        )

        val quantidadeCaes = preferenciasCaes.getInt(
            "quantidade_caes",
            0
        )

        val nomesCaes = mutableListOf<String>()

        // Primeira opção
        nomesCaes.add("Todos os cães")

        // Adiciona todos os cães cadastrados
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

        spinnerFiltroCao.adapter = adapter

        spinnerFiltroCao.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val caoSelecionado =
                        spinnerFiltroCao.selectedItem.toString()

                    mostrarHistorico(
                        caoSelecionado
                    )
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // Não precisa fazer nada
                }
            }
    }

    private fun mostrarHistorico(
        caoSelecionado: String
    ) {

        // Limpa o que estava aparecendo
        layoutRegistros.removeAllViews()

        if (registros.length() == 0) {

            mostrarMensagemVazia(
                "Nenhum registro de cuidado encontrado."
            )

            return
        }

        var encontrouRegistro = false

        // Do mais recente para o mais antigo
        for (i in registros.length() - 1 downTo 0) {

            val registro =
                registros.getJSONObject(i)

            val cao = registro.optString(
                "cao",
                "Cão não informado"
            )

            // Decide se esse registro deve aparecer
            val mostrar =
                caoSelecionado == "Todos os cães" ||
                        cao == caoSelecionado

            if (!mostrar) {
                continue
            }

            encontrouRegistro = true

            val data = registro.optString(
                "data",
                "Data não informada"
            )

            val hora = registro.optString(
                "hora",
                "Hora não informada"
            )

            val cuidados =
                mutableListOf<String>()

            if (registro.optBoolean(
                    "alimentacao",
                    false
                )
            ) {
                cuidados.add("Alimentação")
            }

            if (registro.optBoolean(
                    "agua",
                    false
                )
            ) {
                cuidados.add("Água")
            }

            if (registro.optBoolean(
                    "remedio",
                    false
                )
            ) {
                cuidados.add("Remédio")
            }

            if (registro.optBoolean(
                    "vacina",
                    false
                )
            ) {
                cuidados.add("Vacina")
            }

            if (registro.optBoolean(
                    "cama",
                    false
                )
            ) {
                cuidados.add("Cama")
            }

            if (registro.optBoolean(
                    "cobertor",
                    false
                )
            ) {
                cuidados.add("Cobertor")
            }

            if (registro.optBoolean(
                    "outros",
                    false
                )
            ) {

                val descricao =
                    registro.optString(
                        "descricao_outros",
                        ""
                    )

                if (descricao.isEmpty()) {

                    cuidados.add(
                        "Outros"
                    )

                } else {

                    cuidados.add(
                        "Outros: $descricao"
                    )
                }
            }

            criarBlocoRegistro(
                cao,
                data,
                hora,
                cuidados
            )
        }

        // Caso o cão selecionado não tenha registros
        if (!encontrouRegistro) {

            mostrarMensagemVazia(
                "Nenhum cuidado registrado para $caoSelecionado."
            )
        }
    }

    private fun criarBlocoRegistro(
        cao: String,
        data: String,
        hora: String,
        cuidados: List<String>
    ) {

        val bloco = LinearLayout(this)

        bloco.orientation =
            LinearLayout.VERTICAL

        bloco.setPadding(
            24,
            24,
            24,
            24
        )

        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params.setMargins(
            0,
            0,
            0,
            24
        )

        bloco.layoutParams = params


        // =========================
        // NOME DO CÃO
        // =========================

        val txtCao = TextView(this)

        txtCao.text =
            "🐶 $cao"

        txtCao.textSize = 22f

        txtCao.setTypeface(
            null,
            Typeface.BOLD
        )

        bloco.addView(txtCao)


        // =========================
        // DATA E HORA
        // =========================

        val txtDataHora =
            TextView(this)

        txtDataHora.text =
            "Data: $data\nHora: $hora"

        txtDataHora.textSize = 17f

        txtDataHora.setPadding(
            0,
            12,
            0,
            12
        )

        bloco.addView(txtDataHora)


        // =========================
        // TÍTULO DOS CUIDADOS
        // =========================

        val txtTitulo =
            TextView(this)

        txtTitulo.text =
            "Cuidados realizados:"

        txtTitulo.textSize = 17f

        txtTitulo.setTypeface(
            null,
            Typeface.BOLD
        )

        bloco.addView(txtTitulo)


        // =========================
        // LISTA DE CUIDADOS
        // =========================

        val txtCuidados =
            TextView(this)

        if (cuidados.isEmpty()) {

            txtCuidados.text =
                "Nenhum cuidado selecionado."

        } else {

            txtCuidados.text =
                cuidados.joinToString(
                    "\n"
                ) {
                    "• $it"
                }
        }

        txtCuidados.textSize = 17f

        bloco.addView(txtCuidados)


        // =========================
        // LINHA SEPARADORA
        // =========================

        val separador =
            View(this)

        val separadorParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )

        separadorParams.setMargins(
            0,
            24,
            0,
            0
        )

        separador.layoutParams =
            separadorParams

        separador.setBackgroundColor(
            android.graphics.Color.LTGRAY
        )

        bloco.addView(separador)


        // Adiciona o bloco na tela
        layoutRegistros.addView(bloco)
    }

    private fun mostrarMensagemVazia(
        mensagem: String
    ) {

        val texto =
            TextView(this)

        texto.text = mensagem

        texto.textSize = 18f

        texto.setPadding(
            0,
            16,
            0,
            16
        )

        layoutRegistros.addView(texto)
    }
}