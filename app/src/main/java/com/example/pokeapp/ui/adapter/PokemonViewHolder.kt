package com.example.pokeapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeapp.databinding.ItemPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonListItem

class PokemonViewHolder(private val binding: ItemPokemonBinding) :
RecyclerView.ViewHolder(binding.root){

    fun bind(pokemon: PokemonListItem) {
        binding.txtNomePokemon.text = pokemon.name
        Glide.with(binding.imgPokemon)
            .load(pokemon.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.imgPokemon)

        //clique...
    }
}