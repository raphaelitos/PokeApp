package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.databinding.ItemPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonListItem

class PokemonAdapter() : RecyclerView.Adapter<PokemonViewHolder>() {

    private var pokeList: List<PokemonListItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val item = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return PokemonViewHolder(item)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokeList[position])
    }

    override fun getItemCount(): Int {
        return pokeList.count()
    }

    fun updatePokemonList(list: List<PokemonListItem>) {
        pokeList = list
        notifyDataSetChanged()
    }
}
