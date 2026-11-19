package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentCadastroBinding
import com.example.pokeapp.ui.viewModel.AutenticacaoViewModel
import com.example.pokeapp.ui.viewModel.EstadoAutenticacao

class CadastroFragment : Fragment() {
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AutenticacaoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textVoltarLogin.setOnClickListener { findNavController().popBackStack() }
        binding.btnCadastrar.setOnClickListener {
            val nome = binding.editTextName.text.toString().trim()
            val nick = binding.editTextNick.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val senha = binding.editTextPasswd1.text.toString()
            val confirmacao = binding.editTextPasswd2.text.toString()

            when {
                nome.isBlank() -> binding.editTextName.error = getString(R.string.auth_erro_nome_vazio)
                nick.isBlank() -> binding.editTextNick.error = getString(R.string.auth_erro_nick_vazio)
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    binding.editTextEmail.error = getString(R.string.auth_erro_email_invalido)
                senha.length < 6 -> binding.editTextPasswd1.error = getString(R.string.auth_erro_senha_fraca)
                senha != confirmacao -> binding.editTextPasswd2.error = getString(R.string.auth_erro_senhas_diferentes)
                else -> viewModel.cadastrar(nome, nick, email, senha)
            }
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            val carregando = estado is EstadoAutenticacao.Carregando
            binding.progressCadastro.isVisible = carregando
            binding.btnCadastrar.isEnabled = !carregando
            binding.textVoltarLogin.isEnabled = !carregando
            binding.txtMensagemCadastro.isVisible = estado is EstadoAutenticacao.Erro
            when (estado) {
                is EstadoAutenticacao.Erro -> binding.txtMensagemCadastro.setText(estado.mensagem)
                EstadoAutenticacao.Concluido -> {
                    viewModel.consumirResultado()
                    val opcoes = NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_mng, false)
                        .setLaunchSingleTop(true)
                        .build()
                    findNavController().navigate(R.id.buscaFragment, null, opcoes)
                }
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}