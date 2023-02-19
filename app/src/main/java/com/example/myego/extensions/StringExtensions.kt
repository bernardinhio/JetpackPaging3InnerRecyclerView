package com.example.myego.extensions

// example https://pokeapi.co/api/v2/pokemon/1/
fun String.getPokemonIdFromUrl(): String{
    return this.substringAfter("https://pokeapi.co/api/v2/pokemon/").substringBefore("/")
}