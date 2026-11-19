package com.example.pokeapp.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AutenticacaoRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun estaAutenticado(): Boolean = auth.currentUser != null

    fun entrar(email: String, senha: String, retorno: (Result<Unit>) -> Unit) {
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener { tarefa ->
            if (tarefa.isSuccessful) {
                retorno(Result.success(Unit))
            } else {
                retorno(Result.failure(tarefa.exception ?: IllegalStateException("Falha no login")))
            }
        }
    }

    fun cadastrar(
        nome: String,
        nick: String,
        email: String,
        senha: String,
        retorno: (Result<Unit>) -> Unit
    ) {
        val usuario = auth.currentUser
        if (usuario != null && usuario.email.equals(email, ignoreCase = true)) {
            salvarPerfil(usuario.uid, nome, nick, email, retorno)
            return
        }

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { tarefa ->
            if (!tarefa.isSuccessful) {
                retorno(Result.failure(tarefa.exception ?: IllegalStateException("Falha no cadastro")))
                return@addOnCompleteListener
            }

            val uid = auth.currentUser?.uid
            if (uid == null) {
                retorno(Result.failure(IllegalStateException("Conta sem identificador")))
            } else {
                salvarPerfil(uid, nome, nick, email, retorno)
            }
        }
    }

    private fun salvarPerfil(
        uid: String,
        nome: String,
        nick: String,
        email: String,
        retorno: (Result<Unit>) -> Unit
    ) {
        val perfil = mapOf("nome" to nome, "nick" to nick, "email" to email)
        firestore.collection("usuarios").document(uid).set(perfil)
            .addOnSuccessListener { retorno(Result.success(Unit)) }
            .addOnFailureListener { erro -> retorno(Result.failure(erro)) }
    }

    fun recuperarSenha(email: String, retorno: (Result<Unit>) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { tarefa ->
            if (tarefa.isSuccessful) {
                retorno(Result.success(Unit))
            } else {
                retorno(Result.failure(tarefa.exception ?: IllegalStateException("Falha ao enviar e-mail")))
            }
        }
    }

    fun sair() {
        auth.signOut()
    }
}