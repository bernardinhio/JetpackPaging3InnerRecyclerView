package com.example.myego.datamodel

import com.google.gson.annotations.SerializedName

data class PokemonOverview(
    val name: String? = null,
    @SerializedName("url")
    val urlPokemonDetails: String? = null,

    // additional data for expand/collapse pokemon
    var uiIsExpanded: Boolean = false,
    var uiBaseExperience: Int? = null,
    var uiPokemonId: Int? = null,
    var uiOrder: Int? = null,
    var uiWeight: Int? = null,
    var uiHeight: Int? = null,
    var uiSprites: Sprites? = null,
    var uiDataIsLoading: Boolean = false
)