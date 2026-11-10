package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentListaPokemonBinding
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.viewModel.EstadoListaPokemon
import com.example.pokeapp.ui.viewModel.ListaPokemonViewModel

class ListaPokemonFragment : Fragment() {

    private var _binding: FragmentListaPokemonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListaPokemonViewModel by viewModels()
    private lateinit var adapter: PokemonAdapter

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

        adapter = PokemonAdapter { pokemon ->
            val action = ListaPokemonFragmentDirections
                .actionListaPokemonFragmentToDetalhesPokemonFragment(pokemon.name)
            findNavController().navigate(action)
        }

        binding.recyclerPokemon.layoutManager = GridLayoutManager(requireContext(), COLUNAS)
        binding.recyclerPokemon.adapter = adapter

        // Scroll infinito: ao se aproximar do fim, pede a próxima página à API.
        binding.recyclerPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) carregarSeProximoDoFim()
            }
        })

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            // Depois de aplicar a lista, verifica se a tela ainda não está preenchida.
            adapter.atualizarItens(estado.itens) { _binding?.let { carregarSeProximoDoFim() } }
            apresentarEstado(estado)
        }

        binding.btnTentarNovamente.setOnClickListener {
            viewModel.carregarProximaPagina()
        }

        val atual = viewModel.estado.value
        if (atual != null && atual.itens.isEmpty() && !atual.carregando && atual.erro == null) {
            viewModel.carregarProximaPagina()
        }
    }

    private fun carregarSeProximoDoFim() {
        val estado = viewModel.estado.value ?: return
        // Em caso de erro, o usuário precisa tocar em "Tentar novamente" (evita repetir a falha).
        if (estado.erro != null || estado.carregando || !estado.possuiMais) return

        val layoutManager = binding.recyclerPokemon.layoutManager as GridLayoutManager
        val ultimoVisivel = layoutManager.findLastVisibleItemPosition()
        if (ultimoVisivel >= adapter.itemCount - ITENS_RESTANTES_PARA_CARREGAR) {
            viewModel.carregarProximaPagina()
        }
    }

    private fun apresentarEstado(estado: EstadoListaPokemon) {
        val semItens = estado.itens.isEmpty()

        binding.progressInicial.isVisible = estado.carregando && semItens
        binding.progressPagina.isVisible = estado.carregando && !semItens

        val mensagem = when {
            estado.erro != null -> estado.erro
            semItens && !estado.carregando && !estado.possuiMais ->
                getString(R.string.mensagem_lista_vazia)
            else -> null
        }

        binding.txtEstadoLista.text = mensagem ?: ""
        binding.btnTentarNovamente.isVisible = estado.erro != null
        binding.cardEstado.isVisible = mensagem != null

        // Sem itens o aviso fica no centro; com itens, no rodapé da lista.
        val params = binding.cardEstado.layoutParams as FrameLayout.LayoutParams
        params.gravity =
            if (semItens) Gravity.CENTER else Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        binding.cardEstado.layoutParams = params
    }

    override fun onDestroyView() {
        binding.recyclerPokemon.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        const val COLUNAS = 3
        const val ITENS_RESTANTES_PARA_CARREGAR = 6
    }
}
