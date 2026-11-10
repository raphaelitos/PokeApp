package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.databinding.ItemPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonListItem

class PokemonAdapter(
    private val onPokemonClick: (PokemonListItem) -> Unit
) : RecyclerView.Adapter<PokemonViewHolder>() {

    private var pokemons: List<PokemonListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemons[position], onPokemonClick)
    }

    override fun getItemCount(): Int = pokemons.size

    fun atualizarItens(novosItens: List<PokemonListItem>) {
        pokemons = novosItens
        notifyDataSetChanged()
    }
}
