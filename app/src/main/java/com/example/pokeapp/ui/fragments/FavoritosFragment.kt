package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentFavoritosBinding
import com.example.pokeapp.ui.adapter.FavoritosAdapter
import com.example.pokeapp.ui.util.PokemonUi
import com.example.pokeapp.ui.viewModel.AcaoFavorito
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.example.pokeapp.ui.viewModel.FavoritosViewModel

class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavoritosAdapter(
            onAbrir = { favorito ->
                val args = Bundle().apply { putString("pokemonKey", favorito.name) }
                findNavController().navigate(R.id.detalhesPokemonFragment, args)
            },
            onRemover = { favorito ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialogo_remover_titulo)
                    .setMessage(getString(
                        R.string.dialogo_remover_mensagem,
                        PokemonUi.formatarNome(favorito.name)
                    ))
                    .setNegativeButton(R.string.dialogo_cancelar, null)
                    .setPositiveButton(R.string.dialogo_remover) { _, _ ->
                        viewModel.removerFavorito(favorito.id)
                    }
                    .show()
            }
        )

        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavoritos.adapter = adapter
        binding.btnTentarNovamenteFavoritos.setOnClickListener {
            viewModel.reiniciarObservacaoLista()
        }

        viewModel.estadoLista.observe(viewLifecycleOwner) { estado ->
            adapter.submitList(estado.itens)
            binding.progressFavoritos.isVisible = estado.carregando
            binding.layoutErroFavoritos.isVisible = !estado.carregando &&
                estado.erro != null && estado.itens.isEmpty()
            binding.layoutFavoritosVazio.isVisible = !estado.carregando &&
                estado.erro == null && estado.itens.isEmpty()
            binding.recyclerFavoritos.isVisible = !estado.carregando && estado.itens.isNotEmpty()
            if (estado.erro != null && estado.itens.isNotEmpty()) {
                binding.txtAvisoFavoritos.text = estado.erro
                binding.txtAvisoFavoritos.isVisible = true
            } else {
                binding.txtAvisoFavoritos.isVisible = false
            }
        }

        viewModel.erro.observe(viewLifecycleOwner) { mensagem ->
            if (mensagem != null) {
                viewModel.limparErro()
                Snackbar.make(binding.root, mensagem, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.acaoConcluida.observe(viewLifecycleOwner) { acao ->
            if (acao != null) {
                viewModel.consumirAcaoConcluida()
                val mensagem = when (acao) {
                    AcaoFavorito.ADICIONADO -> R.string.snackbar_favorito_adicionado
                    AcaoFavorito.REMOVIDO -> R.string.snackbar_favorito_removido
                }
                Snackbar.make(binding.root, mensagem, Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.iniciarObservacaoLista()
    }

    override fun onDestroyView() {
        viewModel.pararObservacaoLista()
        binding.recyclerFavoritos.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
