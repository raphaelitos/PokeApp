package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.pokeapp.databinding.ItemPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonListItem

class PokemonAdapter(
    private val onPokemonClick: (PokemonListItem) -> Unit
) : ListAdapter<PokemonListItem, PokemonViewHolder>(Comparador) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position), onPokemonClick)
    }

    /** [onConcluido] é chamado depois que a lista nova foi aplicada ao RecyclerView. */
    fun atualizarItens(novosItens: List<PokemonListItem>, onConcluido: () -> Unit = {}) {
        submitList(novosItens, onConcluido)
    }

    private object Comparador : DiffUtil.ItemCallback<PokemonListItem>() {
        override fun areItemsTheSame(a: PokemonListItem, b: PokemonListItem) = a.name == b.name
        override fun areContentsTheSame(a: PokemonListItem, b: PokemonListItem) = a == b
    }
}
