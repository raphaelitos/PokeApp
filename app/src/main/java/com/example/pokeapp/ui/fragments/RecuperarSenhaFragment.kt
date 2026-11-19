package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentRecuperarSenhaBinding
import com.example.pokeapp.ui.viewModel.AutenticacaoViewModel
import com.example.pokeapp.ui.viewModel.EstadoAutenticacao

class RecuperarSenhaFragment : Fragment() {
    private var _binding: FragmentRecuperarSenhaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AutenticacaoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecuperarSenhaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonVoltarRecuperacao.setOnClickListener { findNavController().popBackStack() }
        binding.buttonEnviarRecuperacao.setOnClickListener {
            val email = binding.editTextEmailRecuperacao.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmailRecuperacao.error = getString(R.string.auth_erro_email_invalido)
            } else {
                viewModel.recuperarSenha(email)
            }
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            val carregando = estado is EstadoAutenticacao.Carregando
            binding.progressRecuperacao.isVisible = carregando
            binding.buttonEnviarRecuperacao.isEnabled = !carregando
            binding.txtMensagemRecuperacao.isVisible =
                estado is EstadoAutenticacao.Erro || estado == EstadoAutenticacao.EmailEnviado
            when (estado) {
                is EstadoAutenticacao.Erro -> binding.txtMensagemRecuperacao.setText(estado.mensagem)
                EstadoAutenticacao.EmailEnviado -> binding.txtMensagemRecuperacao.setText(R.string.auth_recuperacao_enviada)
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
