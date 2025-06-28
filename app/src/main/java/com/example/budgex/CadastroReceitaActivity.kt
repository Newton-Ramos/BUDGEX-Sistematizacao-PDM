package com.example.budgex

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CadastroReceitaActivity : AppCompatActivity() {

    private lateinit var edtDescricao: EditText
    private lateinit var edtValor: EditText
    private lateinit var edtData: EditText
    private lateinit var btnSalvar: Button
    private lateinit var dbHelper: DatabaseHelper

    private val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_receita)

        edtDescricao = findViewById(R.id.edtDescricaoReceita)
        edtValor = findViewById(R.id.edtValorReceita)
        edtData = findViewById(R.id.edtDataReceita)
        btnSalvar = findViewById(R.id.btnSalvarReceita)

        dbHelper = DatabaseHelper(this)

        edtData.setOnClickListener {
            abrirDatePicker()
        }
        edtData.keyListener = null

        btnSalvar.setOnClickListener {
            val descricao = edtDescricao.text.toString().trim()
            val valor = edtValor.text.toString().toDoubleOrNull()
            val dataTexto = edtData.text.toString().trim()

            if (descricao.isBlank() || valor == null || dataTexto.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val receita = Receita(descricao = descricao, valor = valor, data = dataTexto)
            dbHelper.inserirReceita(receita)
            Toast.makeText(this, "Receita salva!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun abrirDatePicker() {
        val calendario = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { _, ano, mes, dia ->
            calendario.set(ano, mes, dia)
            edtData.setText(formatoData.format(calendario.time))
        }

        DatePickerDialog(
            this,
            listener,
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
