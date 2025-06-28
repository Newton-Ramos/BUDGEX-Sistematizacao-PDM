package com.example.budgex

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "budgex.db"
        const val DATABASE_VERSION = 1

        // Tabela de Despesas
        const val TABLE_DESPESAS = "despesas"
        const val COL_ID_DESPESA = "id"
        const val COL_DESCRICAO_DESPESA = "descricao"
        const val COL_VALOR_DESPESA = "valor"
        const val COL_DATA_DESPESA = "data"

        // Tabela de Receitas
        const val TABLE_RECEITAS = "receitas"
        const val COL_ID_RECEITA = "id"
        const val COL_DESCRICAO_RECEITA = "descricao"
        const val COL_VALOR_RECEITA = "valor"
        const val COL_DATA_RECEITA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableDespesas = """
            CREATE TABLE $TABLE_DESPESAS (
                $COL_ID_DESPESA INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DESCRICAO_DESPESA TEXT NOT NULL,
                $COL_VALOR_DESPESA REAL NOT NULL,
                $COL_DATA_DESPESA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableDespesas)

        val createTableReceitas = """
            CREATE TABLE $TABLE_RECEITAS (
                $COL_ID_RECEITA INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DESCRICAO_RECEITA TEXT NOT NULL,
                $COL_VALOR_RECEITA REAL NOT NULL,
                $COL_DATA_RECEITA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableReceitas)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DESPESAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECEITAS")
        onCreate(db)
    }

    fun inserirDespesa(despesa: Despesa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DESCRICAO_DESPESA, despesa.descricao)
            put(COL_VALOR_DESPESA, despesa.valor)
            put(COL_DATA_DESPESA, despesa.data)
        }
        val id = db.insert(TABLE_DESPESAS, null, values)
        db.close()
        return id
    }

    fun inserirReceita(receita: Receita): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DESCRICAO_RECEITA, receita.descricao)
            put(COL_VALOR_RECEITA, receita.valor)
            put(COL_DATA_RECEITA, receita.data)
        }
        val id = db.insert(TABLE_RECEITAS, null, values)
        db.close()
        return id
    }

    fun getAllDespesas(): List<Despesa> {
        val lista = mutableListOf<Despesa>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DESPESAS,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID_DESPESA DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COL_ID_DESPESA))
                val descricao = getString(getColumnIndexOrThrow(COL_DESCRICAO_DESPESA))
                val valor = getDouble(getColumnIndexOrThrow(COL_VALOR_DESPESA))
                val data = getString(getColumnIndexOrThrow(COL_DATA_DESPESA))
                lista.add(Despesa(id, descricao, valor, data))
            }
            close()
        }

        db.close()
        return lista
    }

    fun getAllReceitas(): List<Receita> {
        val lista = mutableListOf<Receita>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_RECEITAS,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID_RECEITA DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COL_ID_RECEITA))
                val descricao = getString(getColumnIndexOrThrow(COL_DESCRICAO_RECEITA))
                val valor = getDouble(getColumnIndexOrThrow(COL_VALOR_RECEITA))
                val data = getString(getColumnIndexOrThrow(COL_DATA_RECEITA))
                lista.add(Receita(id, descricao, valor, data))
            }
            close()
        }

        db.close()
        return lista
    }

    fun deletarDespesa(id: Int): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_DESPESAS, "$COL_ID_DESPESA = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted
    }

    fun deletarReceita(id: Int): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_RECEITAS, "$COL_ID_RECEITA = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted
    }

    // Métodos para deletar todas as despesas e receitas
    fun deletarTodasDespesas(): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_DESPESAS, null, null)
        db.close()
        return rowsDeleted
    }

    fun deletarTodasReceitas(): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_RECEITAS, null, null)
        db.close()
        return rowsDeleted
    }
}
