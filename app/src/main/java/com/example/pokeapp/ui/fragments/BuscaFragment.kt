package com.example.pokeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentBuscaBinding
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuscaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuscaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBuscaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBuscaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navega para a lista de Pokémon.
        binding.btnAbrirLista.setOnClickListener {
            findNavController().navigate(
                R.id.action_buscaFragment_to_listaPokemonFragment
            )
        }

        // Navega para os favoritos.
        binding.btnAbrirFavoritos.setOnClickListener {
            findNavController().navigate(
                R.id.action_buscaFragment_to_favoritosFragment
            )
        }

        // Abre os detalhes de um Pokémon informado pelo usuário.
        binding.btnVerDetalhes.setOnClickListener {

            val pokemon = binding.editPokemon.text
                .toString()
                .trim()
                .lowercase(Locale.ROOT)

            if (pokemon.isBlank()) {
                binding.editPokemon.error = getString(R.string.erro_nome_vazio)
                return@setOnClickListener
            }

            val action =
                BuscaFragmentDirections.actionBuscaFragmentToDetalhesPokemonFragment(pokemon)

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BuscaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuscaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}