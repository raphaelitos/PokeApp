package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentListaPokemonBinding
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.viewModel.EstadoListaPokemon
import com.example.pokeapp.ui.viewModel.ListaPokemonViewModel

class ListaPokemonFragment : Fragment() {

    private var _binding: FragmentListaPokemonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListaPokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PokemonAdapter { pokemon ->
            val action = ListaPokemonFragmentDirections
                .actionListaPokemonFragmentToDetalhesPokemonFragment(pokemon.name)
            findNavController().navigate(action)
        }

        binding.recyclerPokemon.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerPokemon.adapter = adapter

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            adapter.atualizarItens(estado.itens)
            apresentarEstado(estado)
        }

        binding.btnCarregarMais.setOnClickListener {
            viewModel.carregarProximaPagina()
        }
        binding.btnVoltarLista.setOnClickListener {
            findNavController().popBackStack()
        }

        val atual = viewModel.estado.value
        if (atual != null && atual.itens.isEmpty() && !atual.carregando && atual.erro == null) {
            viewModel.carregarProximaPagina()
        }
    }

    private fun apresentarEstado(estado: EstadoListaPokemon) {
        binding.btnCarregarMais.visibility = if (estado.possuiMais) View.VISIBLE else View.GONE
        binding.btnCarregarMais.isEnabled = !estado.carregando

        val mensagem = when {
            estado.carregando -> getString(R.string.mensagem_carregando_lista)
            estado.erro != null -> estado.erro
            estado.itens.isEmpty() && !estado.possuiMais -> getString(R.string.mensagem_lista_vazia)
            else -> null
        }
        binding.txtEstadoLista.text = mensagem ?: ""
        binding.txtEstadoLista.visibility = if (mensagem == null) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        binding.recyclerPokemon.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
