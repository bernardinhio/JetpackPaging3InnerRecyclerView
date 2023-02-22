package com.example.myego.api

import com.example.myego.datamodel.PokemonDetails
import com.example.myego.datamodel.Pokemons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    companion object{

        const val BASE_URL = "https://pokeapi.co/api/"
        const val END_POINT = "v2/pokemon"
    }

    @GET(END_POINT)
    suspend fun getAllPokemons(
        @Query("offset") offset: Int?,
        @Query("api_key") limit: Int?
    ): Response<Pokemons>


    @GET("$END_POINT/{pokemon_id}")
    suspend fun getPokemonDetails(@Path(value = "pokemon_id", encoded = true) pokemonId: String): Response<PokemonDetails>

}