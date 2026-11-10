package com.example.pokeapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ItemPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonListItem
import com.example.pokeapp.ui.util.PokemonUi

class PokemonViewHolder(private val binding: ItemPokemonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pokemon: PokemonListItem, onPokemonClick: (PokemonListItem) -> Unit) {
        val context = binding.root.context
        val nome = PokemonUi.formatarNome(pokemon.name)

        binding.txtNomePokemon.text = nome
        binding.txtNumeroPokemon.text = context.getString(R.string.numero_pokemon, pokemon.id)
        binding.root.contentDescription = nome

        Glide.with(binding.imgPokemon)
            .load(pokemon.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.imgPokemon)

        binding.root.setOnClickListener { onPokemonClick(pokemon) }
    }
}
