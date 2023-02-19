package com.example.myego.datamodel

import com.google.gson.annotations.SerializedName

data class Sprites(
    @SerializedName("front_default")
    val frontDefaultImage: String? = null,
    @SerializedName("back_default")
    val backDefaultImage: String? = null,

    @SerializedName("front_shiny")
    val frontShinyImage: String? = null,
    @SerializedName("back_shiny")
    val backShinyImage: String? = null,

    @SerializedName("front_female")
    val frontFemaleImage: String? = null,
    @SerializedName("back_female")
    val backFemaleImage: String? = null,

    @SerializedName("front_shiny_female")
    val frontShinyFemaleImage: String? = null,
    @SerializedName("back_shiny_female")
    val backShinyFemaleImage: String? = null
)