package com.example.pokeapp.ui.viewModel

import androidx.annotation.StringRes

sealed class EstadoAutenticacao {
    object Inicial : EstadoAutenticacao()
    object Carregando : EstadoAutenticacao()
    object Concluido : EstadoAutenticacao()
    object EmailEnviado : EstadoAutenticacao()
    data class Erro(@param:StringRes val mensagem: Int) : EstadoAutenticacao()
}