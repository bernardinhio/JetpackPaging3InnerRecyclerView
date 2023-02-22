package com.example.myego.datamodel

import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    val name: String? = null,
    val id: Int? = null,
    @SerializedName("base_experience")
    val baseExperience: Int? = null,
    val order: Int? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val sprites: Sprites? = null,
)