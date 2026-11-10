package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ItemFavoritoBinding
import com.example.pokeapp.repository.local.PokemonFavorito
import com.example.pokeapp.ui.util.PokemonUi

class FavoritosAdapter(
    private val onAbrir: (PokemonFavorito) -> Unit,
    private val onRemover: (PokemonFavorito) -> Unit
) : ListAdapter<PokemonFavorito, FavoritosAdapter.FavoritoViewHolder>(Comparador) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val binding = ItemFavoritoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoritoViewHolder(private val binding: ItemFavoritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorito: PokemonFavorito) {
            val context = binding.root.context
            val nome = PokemonUi.formatarNome(favorito.name)

            binding.txtNomeFavorito.text = nome
            binding.txtNumeroFavorito.text = context.getString(R.string.numero_pokemon, favorito.id)
            binding.root.contentDescription = nome

            Glide.with(binding.imgFavorito)
                .load(PokemonUi.urlArtwork(favorito.id))
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.imgFavorito)

            binding.root.setOnClickListener { onAbrir(favorito) }
            binding.btnRemoverFavorito.setOnClickListener { onRemover(favorito) }
        }
    }

    private object Comparador : DiffUtil.ItemCallback<PokemonFavorito>() {
        override fun areItemsTheSame(a: PokemonFavorito, b: PokemonFavorito) = a.id == b.id
        override fun areContentsTheSame(a: PokemonFavorito, b: PokemonFavorito) = a == b
    }
}
