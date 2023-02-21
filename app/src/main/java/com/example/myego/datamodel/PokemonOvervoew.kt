package com.example.myego.datamodel

import com.google.gson.annotations.SerializedName

data class PokemonOverview(
    val name: String? = null,
    @SerializedName("url")
    val urlPokemonDetails: String? = null,

    // used to expand / collapse
    var isExpanded: Boolean = false,
    var baseExperience: Int? = null,
    var id: Int? = null,
    var order: Int? = null,
    var weight: Int? = null,
    var height: Int? = null,
    var sprites: Sprites? = null,
)