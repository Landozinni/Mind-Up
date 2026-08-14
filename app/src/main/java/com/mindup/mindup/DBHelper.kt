package com.mindup.mindup

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

sealed class AuthResult {
    object Success : AuthResult()
    object EmptyFields : AuthResult()
    object InvalidEmail : AuthResult()
    object InvalidPassword : AuthResult()
    object EmailExists : AuthResult()
    object UserNotFound : AuthResult()
    object WrongPassword : AuthResult()
    object Error : AuthResult()
}

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "app.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                data_nascimento TEXT,
                email TEXT UNIQUE,
                senha TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS diario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                humor TEXT,
                descricao TEXT,
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dass21_resultados (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                depressao_score INTEGER,
                depressao_classificacao TEXT,
                ansiedade_score INTEGER,
                ansiedade_classificacao TEXT,
                estresse_score INTEGER,
                estresse_classificacao TEXT,
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS diario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    humor TEXT,
                    descricao TEXT,
                    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """.trimIndent()
            )
        }
        if (oldVersion < 3) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS dass21_resultados (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    depressao_score INTEGER,
                    depressao_classificacao TEXT,
                    ansiedade_score INTEGER,
                    ansiedade_classificacao TEXT,
                    estresse_score INTEGER,
                    estresse_classificacao TEXT,
                    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """.trimIndent()
            )
        }
    }

    fun register(
        nome: String,
        nascimento: String,
        email: String,
        senha: String
    ): AuthResult {

        val nomeLimpo = nome.trim()
        val nascimentoLimpo = nascimento.trim()
        val emailLimpo = email.trim()
        val senhaLimpa = senha.trim()

        if (
            nomeLimpo.isEmpty() ||
            nascimentoLimpo.isEmpty() ||
            emailLimpo.isEmpty() ||
            senhaLimpa.isEmpty()
        ) {
            return AuthResult.EmptyFields
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailLimpo).matches()) {
            return AuthResult.InvalidEmail
        }

        if (senhaLimpa.length < 6) {
            return AuthResult.InvalidPassword
        }

        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM users WHERE email = ?",
            arrayOf(emailLimpo)
        )

        val exists = cursor.moveToFirst()
        cursor.close()

        if (exists) {
            return AuthResult.EmailExists
        }

        val values = ContentValues().apply {
            put("nome", nomeLimpo)
            put("data_nascimento", nascimentoLimpo)
            put("email", emailLimpo)
            put("senha", senhaLimpa)
        }

        val result = db.insert("users", null, values)

        return if (result != -1L) {
            AuthResult.Success
        } else {
            AuthResult.Error
        }
    }

    fun login(email: String, senha: String): AuthResult {

        val emailLimpo = email.trim()
        val senhaLimpa = senha.trim()

        if (emailLimpo.isEmpty() || senhaLimpa.isEmpty()) {
            return AuthResult.EmptyFields
        }

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT senha FROM users WHERE email = ?",
            arrayOf(emailLimpo)
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            return AuthResult.UserNotFound
        }

        val senhaBanco = cursor.getString(0)
        cursor.close()

        return if (senhaBanco == senhaLimpa) {
            AuthResult.Success
        } else {
            AuthResult.WrongPassword
        }
    }

    fun salvarEntradaDiario(humor: String, descricao: String): Boolean {
        val humorLimpo = humor.trim()
        val descricaoLimpa = descricao.trim()

        val db = writableDatabase
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS diario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                humor TEXT,
                descricao TEXT,
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """.trimIndent()
        )

        val values = ContentValues().apply {
            put("humor", humorLimpo)
            put("descricao", descricaoLimpa)
        }

        val result = db.insert("diario", null, values)
        return result != -1L
    }

    fun salvarResultadoDass21(
        depressaoScore: Int,
        depressaoClassificacao: String,
        ansiedadeScore: Int,
        ansiedadeClassificacao: String,
        estresseScore: Int,
        estresseClassificacao: String
    ): Boolean {
        val db = writableDatabase
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dass21_resultados (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                depressao_score INTEGER,
                depressao_classificacao TEXT,
                ansiedade_score INTEGER,
                ansiedade_classificacao TEXT,
                estresse_score INTEGER,
                estresse_classificacao TEXT,
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """.trimIndent()
        )

        val values = ContentValues().apply {
            put("depressao_score", depressaoScore)
            put("depressao_classificacao", depressaoClassificacao)
            put("ansiedade_score", ansiedadeScore)
            put("ansiedade_classificacao", ansiedadeClassificacao)
            put("estresse_score", estresseScore)
            put("estresse_classificacao", estresseClassificacao)
        }

        val result = db.insert("dass21_resultados", null, values)
        return result != -1L
    }
}