package com.example.pokeapp.repository.api.model

data class PokemonListResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItem> = emptyList()
)

data class PokemonListItem(
    val name: String = "",
    val url: String = ""
) {

    // Obtém o identificador a partir da URL do recurso.
    val id: Int
        get() = url
            .trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull() ?: 0

    // Utiliza a arte oficial do Pokémon.
    val imageUrl: String?
        get() = if (id > 0) {
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/" +
                    "sprites/pokemon/other/official-artwork/$id.png"
        } else {
            null
        }
}