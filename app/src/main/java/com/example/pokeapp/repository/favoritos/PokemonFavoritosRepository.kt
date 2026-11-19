package com.example.pokeapp.repository.favoritos

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class PokemonFavoritosRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private fun colecaoDoUsuario(uid: String): CollectionReference = firestore
        .collection("usuarios")
        .document(uid)
        .collection("favoritos")

    fun observarTodos(
        aoReceber: (List<PokemonFavorito>) -> Unit,
        aoErro: (Exception) -> Unit
    ): ListenerRegistration? {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            aoErro(IllegalStateException("Entre na sua conta para consultar os favoritos."))
            return null
        }

        return colecaoDoUsuario(uid)
            .orderBy("id", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, erro ->
                if (auth.currentUser?.uid != uid) return@addSnapshotListener
                if (erro != null) {
                    aoErro(erro)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val favoritos = snapshot.documents.mapNotNull { documento ->
                        val id = documento.getLong("id")?.toInt()
                        val nome = documento.getString("name")
                        if (id != null && id > 0 && !nome.isNullOrBlank()) {
                            PokemonFavorito(id, nome)
                        } else null
                    }
                    aoReceber(favoritos)
                }
            }
    }

    fun observarFavorito(
        id: Int,
        aoReceber: (Boolean) -> Unit,
        aoErro: (Exception) -> Unit
    ): ListenerRegistration? {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            aoErro(IllegalStateException("Entre na sua conta para consultar os favoritos."))
            return null
        }

        return colecaoDoUsuario(uid).document(id.toString())
            .addSnapshotListener { documento, erro ->
                if (auth.currentUser?.uid != uid) return@addSnapshotListener
                if (erro != null) {
                    aoErro(erro)
                } else if (documento != null) {
                    aoReceber(documento.exists())
                }
            }
    }

    fun adicionar(pokemon: PokemonFavorito, retorno: (Result<Unit>) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            retorno(Result.failure(IllegalStateException("Sessão encerrada. Faça login novamente.")))
            return
        }
        colecaoDoUsuario(uid).document(pokemon.id.toString())
            .set(mapOf("id" to pokemon.id, "name" to pokemon.name))
            .addOnSuccessListener { retorno(Result.success(Unit)) }
            .addOnFailureListener { retorno(Result.failure(it)) }
    }

    fun remover(id: Int, retorno: (Result<Unit>) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            retorno(Result.failure(IllegalStateException("Sessão encerrada. Faça login novamente.")))
            return
        }
        colecaoDoUsuario(uid).document(id.toString())
            .delete()
            .addOnSuccessListener { retorno(Result.success(Unit)) }
            .addOnFailureListener { retorno(Result.failure(it)) }
    }
}
