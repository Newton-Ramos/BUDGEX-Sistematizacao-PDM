package com.example.budgex

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EditarReceitaActivity : AppCompatActivity() {

    private lateinit var edtDescricao: EditText
    private lateinit var edtValor: EditText
    private lateinit var edtData: EditText
    private lateinit var btnSalvar: Button
    private lateinit var dbHelper: DatabaseHelper
    private var receitaId = -1
    private val calendario = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_receita)

        edtDescricao = findViewById(R.id.edtDescricao)
        edtValor = findViewById(R.id.edtValor)
        edtData = findViewById(R.id.edtData)
        btnSalvar = findViewById(R.id.btnSalvar)
        dbHelper = DatabaseHelper(this)

        receitaId = intent.getIntExtra("RECEITA_ID", -1)
        if (receitaId != -1) {
            val receita = dbHelper.getReceitaById(receitaId)
            receita?.let {
                edtDescricao.setText(it.descricao)
                edtValor.setText(it.valor.toString())
                edtData.setText(it.data)
            }
        } else {
            Toast.makeText(this, "Erro ao carregar a receita", Toast.LENGTH_SHORT).show()
            finish()
        }

        edtData.setOnClickListener {
            val ano = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                val dataSelecionada = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                }
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                edtData.setText(formato.format(dataSelecionada.time))
            }, ano, mes, dia).show()
        }

        btnSalvar.setOnClickListener {
            val descricao = edtDescricao.text.toString().trim()
            val valor = edtValor.text.toString().toDoubleOrNull()
            val data = edtData.text.toString().trim()

            if (descricao.isBlank() || valor == null || valor <= 0 || data.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val atualizado = dbHelper.atualizarReceita(receitaId, descricao, valor, data)
            if (atualizado > 0) {
                Toast.makeText(this, "Receita atualizada com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao atualizar a receita", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
