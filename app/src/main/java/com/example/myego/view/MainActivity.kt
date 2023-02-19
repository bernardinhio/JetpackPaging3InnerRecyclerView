package com.example.myego.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myego.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Making recyclerView ADAPTER Injectable and Inject in it Repository
 *
 * Instead of Initializing the Adapter in a Fragment, we can inject it
 * Instead of this:
 * lateinit var pokemonPagingDataAdapter: PokemonPagingDataAdapter
 * and
 * pokemonPagingDataAdapter = PokemonPagingDataAdapter()
 *
 * We can:
 * @Inject
 * lateinit var pokemonPagingDataAdapter: PokemonPagingDataAdapter
 *After we tag the Adapter with @Singleton
 * and we inject some injectable values with:
 *  @Inject constructor(---)
 *
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}