package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapp.R
import com.example.pokeapp.repository.auth.AutenticacaoRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.firestore.FirebaseFirestoreException

class AutenticacaoViewModel : ViewModel() {
    private val repository = AutenticacaoRepository()
    private val _estado = MutableLiveData<EstadoAutenticacao>(EstadoAutenticacao.Inicial)
    val estado: LiveData<EstadoAutenticacao> = _estado

    fun estaAutenticado(): Boolean = repository.estaAutenticado()

    fun entrar(email: String, senha: String) {
        _estado.value = EstadoAutenticacao.Carregando
        repository.entrar(email, senha) { resultado ->
            _estado.value = resultado.fold(
                onSuccess = { EstadoAutenticacao.Concluido },
                onFailure = { EstadoAutenticacao.Erro(mensagemDeErro(it)) }
            )
        }
    }

    fun cadastrar(nome: String, nick: String, email: String, senha: String) {
        _estado.value = EstadoAutenticacao.Carregando
        repository.cadastrar(nome, nick, email, senha) { resultado ->
            _estado.value = resultado.fold(
                onSuccess = { EstadoAutenticacao.Concluido },
                onFailure = { EstadoAutenticacao.Erro(mensagemDeErro(it)) }
            )
        }
    }

    fun recuperarSenha(email: String) {
        _estado.value = EstadoAutenticacao.Carregando
        repository.recuperarSenha(email) { resultado ->
            _estado.value = resultado.fold(
                onSuccess = { EstadoAutenticacao.EmailEnviado },
                onFailure = { EstadoAutenticacao.Erro(mensagemDeErro(it)) }
            )
        }
    }

    fun consumirResultado() {
        _estado.value = EstadoAutenticacao.Inicial
    }

    private fun mensagemDeErro(erro: Throwable): Int = when (erro) {
        is FirebaseAuthUserCollisionException -> R.string.auth_erro_email_existente
        is FirebaseAuthWeakPasswordException -> R.string.auth_erro_senha_fraca
        is FirebaseAuthInvalidCredentialsException -> R.string.auth_erro_credenciais
        is FirebaseTooManyRequestsException -> R.string.auth_erro_muitas_tentativas
        is FirebaseNetworkException -> R.string.auth_erro_rede
        is FirebaseFirestoreException -> R.string.auth_erro_perfil
        else -> R.string.auth_erro_generico
    }
}