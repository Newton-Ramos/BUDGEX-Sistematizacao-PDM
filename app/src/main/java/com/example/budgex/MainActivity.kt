package com.example.budgex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddDespesa: FloatingActionButton
    private lateinit var fabAddReceita: FloatingActionButton
    private lateinit var btnMostrarDespesas: Button
    private lateinit var btnMostrarReceitas: Button
    private lateinit var btnClearAll: Button
    private lateinit var tvTotal: TextView
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var despesaAdapter: DespesaAdapter
    private lateinit var receitaAdapter: ReceitaAdapter

    private var mostrandoDespesas = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa views
        recyclerView = findViewById(R.id.recyclerViewDespesas)
        fabAddDespesa = findViewById(R.id.fabAddDespesa)
        fabAddReceita = findViewById(R.id.fabAddReceita)
        btnMostrarDespesas = findViewById(R.id.btnMostrarDespesas)
        btnMostrarReceitas = findViewById(R.id.btnMostrarReceitas)
        btnClearAll = findViewById(R.id.btnClearAll)
        tvTotal = findViewById(R.id.tvTotal)

        dbHelper = DatabaseHelper(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter de Despesas
        despesaAdapter = DespesaAdapter(
            mutableListOf(),
            onItemClick = { /* opcional */ },
            onItemDelete = { despesa ->
                val rowsDeleted = dbHelper.deletarDespesa(despesa.id)
                if (rowsDeleted > 0) mostrarDespesas()
            },
            onItemEdit = { despesa ->
                val intent = Intent(this, EditarDespesaActivity::class.java)
                intent.putExtra("DESPESA_ID", despesa.id)
                startActivity(intent)
            }
        )

        // Adapter de Receitas
        receitaAdapter = ReceitaAdapter(
            mutableListOf(),
            onItemClick = { /* opcional */ },
            onItemDelete = { receita ->
                val rowsDeleted = dbHelper.deletarReceita(receita.id)
                if (rowsDeleted > 0) mostrarReceitas()
            },
            onItemEdit = { receita ->
                val intent = Intent(this, EditarReceitaActivity::class.java)
                intent.putExtra("RECEITA_ID", receita.id)
                startActivity(intent)
            }
        )

        recyclerView.adapter = despesaAdapter // inicia com despesas

        // Ações dos FABs e Botões
        fabAddDespesa.setOnClickListener {
            startActivity(Intent(this, CadastroDespesaActivity::class.java))
        }

        fabAddReceita.setOnClickListener {
            startActivity(Intent(this, CadastroReceitaActivity::class.java))
        }

        btnMostrarDespesas.setOnClickListener {
            mostrarDespesas()
        }

        btnMostrarReceitas.setOnClickListener {
            mostrarReceitas()
        }

        btnClearAll.setOnClickListener {
            if (mostrandoDespesas) {
                val rowsDeleted = dbHelper.deletarTodasDespesas()
                if (rowsDeleted > 0) mostrarDespesas()
            } else {
                val rowsDeleted = dbHelper.deletarTodasReceitas()
                if (rowsDeleted > 0) mostrarReceitas()
            }
        }

        mostrarDespesas() // carrega despesas por padrão
    }

    override fun onResume() {
        super.onResume()
        if (mostrandoDespesas) mostrarDespesas() else mostrarReceitas()
    }

    private fun mostrarDespesas() {
        mostrandoDespesas = true
        val despesas = dbHelper.getAllDespesas()
        despesaAdapter.atualizarLista(despesas)
        recyclerView.adapter = despesaAdapter

        val total = despesas.sumOf { it.valor }
        tvTotal.text = getString(R.string.total_despesas, total)
    }

    private fun mostrarReceitas() {
        mostrandoDespesas = false
        val receitas = dbHelper.getAllReceitas()
        receitaAdapter.atualizarLista(receitas)
        recyclerView.adapter = receitaAdapter

        val total = receitas.sumOf { it.valor }
        tvTotal.text = getString(R.string.total_receitas, total)
    }
}
