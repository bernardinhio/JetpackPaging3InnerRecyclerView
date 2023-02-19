package com.example.myego.datamodel

import com.example.myego.datamodel.Sprites
import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    val name: String? = null,
    @SerializedName("base_experience")
    val baseExperience: Int? = null,
    val id: Int? = null,  // maybe all are Int
    val order: Int? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val sprites: Sprites? = null,
)