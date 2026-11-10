package com.example.pokeapp.ui.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.pokeapp.R
import java.util.Locale

/** Funções auxiliares de apresentação: nomes, cores e imagens dos Pokémon. */
object PokemonUi {

    private const val BASE_ARTWORK = "https://raw.githubusercontent.com/PokeAPI/sprites/master/" +
            "sprites/pokemon/other/official-artwork/"

    fun urlArtwork(id: Int): String = "$BASE_ARTWORK$id.png"

    /** "mr-mime" -> "Mr Mime" */
    fun formatarNome(nome: String): String =
        nome.split('-', ' ')
            .filter { it.isNotEmpty() }
            .joinToString(" ") { parte ->
                parte.replaceFirstChar { it.titlecase(Locale.ROOT) }
            }

    fun nomeDoTipo(context: Context, tipo: String): String =
        recursoDoTipo(tipo)?.let { context.getString(it.second) } ?: formatarNome(tipo)

    fun corDoTipo(context: Context, tipo: String): Int =
        ContextCompat.getColor(
            context,
            recursoDoTipo(tipo)?.first ?: R.color.poke_type_normal
        )

    fun nomeDoStat(context: Context, stat: String): String =
        when (stat) {
            "hp" -> R.string.stat_hp
            "attack" -> R.string.stat_attack
            "defense" -> R.string.stat_defense
            "special-attack" -> R.string.stat_special_attack
            "special-defense" -> R.string.stat_special_defense
            "speed" -> R.string.stat_speed
            else -> null
        }?.let { context.getString(it) } ?: formatarNome(stat)

    private fun recursoDoTipo(tipo: String): Pair<Int, Int>? =
        when (tipo) {
            "normal" -> R.color.poke_type_normal to R.string.tipo_normal
            "fire" -> R.color.poke_type_fire to R.string.tipo_fire
            "water" -> R.color.poke_type_water to R.string.tipo_water
            "grass" -> R.color.poke_type_grass to R.string.tipo_grass
            "electric" -> R.color.poke_type_electric to R.string.tipo_electric
            "ice" -> R.color.poke_type_ice to R.string.tipo_ice
            "fighting" -> R.color.poke_type_fighting to R.string.tipo_fighting
            "poison" -> R.color.poke_type_poison to R.string.tipo_poison
            "ground" -> R.color.poke_type_ground to R.string.tipo_ground
            "flying" -> R.color.poke_type_flying to R.string.tipo_flying
            "psychic" -> R.color.poke_type_psychic to R.string.tipo_psychic
            "bug" -> R.color.poke_type_bug to R.string.tipo_bug
            "rock" -> R.color.poke_type_rock to R.string.tipo_rock
            "ghost" -> R.color.poke_type_ghost to R.string.tipo_ghost
            "dragon" -> R.color.poke_type_dragon to R.string.tipo_dragon
            "dark" -> R.color.poke_type_dark to R.string.tipo_dark
            "steel" -> R.color.poke_type_steel to R.string.tipo_steel
            "fairy" -> R.color.poke_type_fairy to R.string.tipo_fairy
            else -> null
        }
}
