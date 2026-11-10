package com.example.pokeapp.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PokemonFavorito::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun favoritosDao(): PokemonFavoritoDao

    companion object {
        @Volatile
        private var instance: PokemonDatabase? = null

        fun obter(context: Context): PokemonDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokeapp.db"
                ).build().also { instance = it }
            }
    }
}

