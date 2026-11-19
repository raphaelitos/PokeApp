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
import com.example.pokeapp.databinding.FragmentLoginBinding
import com.example.pokeapp.ui.viewModel.AutenticacaoViewModel
import com.example.pokeapp.ui.viewModel.EstadoAutenticacao

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AutenticacaoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
        binding.textRecuperarSenha.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperarSenhaFragment)
        }
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val senha = binding.editTextPasswd.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = getString(R.string.auth_erro_email_invalido)
            } else if (senha.isBlank()) {
                binding.editTextPasswd.error = getString(R.string.auth_erro_senha_vazia)
            } else {
                viewModel.entrar(email, senha)
            }
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            val carregando = estado is EstadoAutenticacao.Carregando
            binding.progressLogin.isVisible = carregando
            binding.buttonLogin.isEnabled = !carregando
            binding.textRegister.isEnabled = !carregando
            binding.textRecuperarSenha.isEnabled = !carregando
            binding.txtMensagemLogin.isVisible = estado is EstadoAutenticacao.Erro
            when (estado) {
                is EstadoAutenticacao.Erro -> binding.txtMensagemLogin.setText(estado.mensagem)
                EstadoAutenticacao.Concluido -> {
                    viewModel.consumirResultado()
                    abrirPokedex()
                }
                else -> Unit
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.estaAutenticado() && findNavController().currentDestination?.id == R.id.loginFragment) {
            abrirPokedex()
        }
    }

    private fun abrirPokedex() {
        val controller = findNavController()
        val opcoes = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_mng, false)
            .setLaunchSingleTop(true)
            .build()
        controller.navigate(R.id.buscaFragment, null, opcoes)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}