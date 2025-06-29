package com.example.budgex

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "budgex.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableDespesas = """
            CREATE TABLE despesas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descricao TEXT NOT NULL,
                valor REAL NOT NULL,
                data TEXT NOT NULL
            );
        """.trimIndent()

        val createTableReceitas = """
            CREATE TABLE receitas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descricao TEXT NOT NULL,
                valor REAL NOT NULL,
                data TEXT NOT NULL
            );
        """.trimIndent()

        db.execSQL(createTableDespesas)
        db.execSQL(createTableReceitas)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS despesas")
        db.execSQL("DROP TABLE IF EXISTS receitas")
        onCreate(db)
    }

    // --- Inserir Despesa
    fun inserirDespesa(despesa: Despesa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("descricao", despesa.descricao)
            put("valor", despesa.valor)
            put("data", despesa.data)
        }
        return db.insert("despesas", null, values)
    }

    // --- Inserir Receita
    fun inserirReceita(receita: Receita): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("descricao", receita.descricao)
            put("valor", receita.valor)
            put("data", receita.data)
        }
        return db.insert("receitas", null, values)
    }

    // --- Métodos para Despesas ---

    fun getDespesaById(id: Int): Despesa? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM despesas WHERE id = ?", arrayOf(id.toString()))
        val despesa = if (cursor.moveToFirst()) {
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
            val data = cursor.getString(cursor.getColumnIndexOrThrow("data"))
            Despesa(id, descricao, valor, data)
        } else null
        cursor.close()
        return despesa
    }

    fun atualizarDespesa(id: Int, descricao: String, valor: Double, data: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("descricao", descricao)
            put("valor", valor)
            put("data", data)
        }
        return db.update("despesas", values, "id = ?", arrayOf(id.toString()))
    }

    fun deletarDespesa(id: Int): Int {
        val db = writableDatabase
        return db.delete("despesas", "id = ?", arrayOf(id.toString()))
    }

    fun deletarTodasDespesas(): Int {
        val db = writableDatabase
        return db.delete("despesas", null, null)
    }

    fun getAllDespesas(): List<Despesa> {
        val lista = mutableListOf<Despesa>()
        val db = readableDatabase
        val cursor = db.query("despesas", null, null, null, null, null, "data DESC")
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val descricao = it.getString(it.getColumnIndexOrThrow("descricao"))
                    val valor = it.getDouble(it.getColumnIndexOrThrow("valor"))
                    val data = it.getString(it.getColumnIndexOrThrow("data"))
                    lista.add(Despesa(id, descricao, valor, data))
                } while (it.moveToNext())
            }
        }
        return lista
    }

    // --- Métodos para Receitas ---

    fun getReceitaById(id: Int): Receita? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM receitas WHERE id = ?", arrayOf(id.toString()))
        val receita = if (cursor.moveToFirst()) {
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
            val data = cursor.getString(cursor.getColumnIndexOrThrow("data"))
            Receita(id, descricao, valor, data)
        } else null
        cursor.close()
        return receita
    }

    fun atualizarReceita(id: Int, descricao: String, valor: Double, data: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("descricao", descricao)
            put("valor", valor)
            put("data", data)
        }
        return db.update("receitas", values, "id = ?", arrayOf(id.toString()))
    }

    fun deletarReceita(id: Int): Int {
        val db = writableDatabase
        return db.delete("receitas", "id = ?", arrayOf(id.toString()))
    }

    fun deletarTodasReceitas(): Int {
        val db = writableDatabase
        return db.delete("receitas", null, null)
    }

    fun getAllReceitas(): List<Receita> {
        val lista = mutableListOf<Receita>()
        val db = readableDatabase
        val cursor = db.query("receitas", null, null, null, null, null, "data DESC")
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val descricao = it.getString(it.getColumnIndexOrThrow("descricao"))
                    val valor = it.getDouble(it.getColumnIndexOrThrow("valor"))
                    val data = it.getString(it.getColumnIndexOrThrow("data"))
                    lista.add(Receita(id, descricao, valor, data))
                } while (it.moveToNext())
            }
        }
        return lista
    }
}
