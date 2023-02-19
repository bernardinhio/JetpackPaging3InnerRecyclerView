package com.example.myego.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myego.databinding.ItemlistPokemonsBinding
import com.example.myego.datamodel.PokemonPhoto
import com.example.myego.datamodel.Pokemons
import com.example.myego.datamodel.Sprites
import com.example.myego.extensions.getPokemonIdFromUrl
import com.example.myego.view.MainFragment
import com.example.myego.viewmodel.MainFragmentViewModel
import javax.inject.Inject

class PokemonsPagingDataAdapter @Inject constructor(
    val viewModel: MainFragmentViewModel,
    val mainFragment: MainFragment
) : PagingDataAdapter<Pokemons.PokemonOverview, PokemonsPagingDataAdapter.PokemonViewHolder>(
    PokemonsDiffUtilItemCallback()
) {

    inner class PokemonViewHolder(private val viewBinding: ItemlistPokemonsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: Pokemons.PokemonOverview) {
            viewBinding.tvName.text = data.name.orEmpty().uppercase()

            val pokemonDetailsUrl: String = data.urlPokemonDetails.orEmpty()
            val pokemonId = pokemonDetailsUrl.getPokemonIdFromUrl()

            viewModel.pokemonDetailsLiveData.observe(
                mainFragment.viewLifecycleOwner,
                { pokemonDetails ->
                    viewBinding.tvName.text = pokemonDetails.name.orEmpty().uppercase()
                    viewBinding.tvBaseExperience.text =
                        "Base Experience: ${pokemonDetails.baseExperience?.toString()}"
                    viewBinding.tvId.text = "Id: ${pokemonDetails.id?.toString()}"
                    viewBinding.tvOrder.text = "Order: ${pokemonDetails.order?.toString()}"
                    viewBinding.tvHeight.text = "Height: ${pokemonDetails.height?.toString()}"
                    viewBinding.tvWeight.text = "Weight: ${pokemonDetails.weight?.toString()}"

                    val sprinters = pokemonDetails.sprites
                    setupPokemonPhotosRecyclerView(viewBinding.rvPokemonPhotos, sprinters)
                }
            )

            viewModel.fetchPokemonDetails(pokemonId)
        }

        fun setupPokemonPhotosRecyclerView(
            rvPokemonPhotos: RecyclerView,
            sprinters: Sprites?
        ) {

            val imagesList = mutableListOf<PokemonPhoto>().apply {
                add(PokemonPhoto("Front Default", sprinters?.frontDefaultImage))
                add(PokemonPhoto("Back Default", sprinters?.backDefaultImage))
                add(PokemonPhoto("Front Shiny", sprinters?.frontShinyImage))
                add(PokemonPhoto("Back Shiny", sprinters?.backShinyImage))
                add(PokemonPhoto("Front Female", sprinters?.frontFemaleImage))
                add(PokemonPhoto("Back Female", sprinters?.backFemaleImage))
                add(PokemonPhoto("Front Shiny Female", sprinters?.frontShinyFemaleImage))
                add(PokemonPhoto("Back Shiny Female", sprinters?.backShinyFemaleImage))
            }

            val rvAdapter = PokemonPhotosAdapter()

            with(rvPokemonPhotos) {
                layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)

                adapter = rvAdapter

                (adapter as PokemonPhotosAdapter).submitList(imagesList)

            }
        }

    }

    class PokemonsDiffUtilItemCallback : DiffUtil.ItemCallback<Pokemons.PokemonOverview>() {
        override fun areItemsTheSame(
            oldItem: Pokemons.PokemonOverview,
            newItem: Pokemons.PokemonOverview
        ): Boolean {
            return newItem.hashCode() == oldItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: Pokemons.PokemonOverview,
            newItem: Pokemons.PokemonOverview
        ): Boolean {
            return oldItem.urlPokemonDetails == newItem.urlPokemonDetails
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            ItemlistPokemonsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentDataItem: Pokemons.PokemonOverview? = this.getItem(position)
        currentDataItem?.let { holder.bind(it) }
    }

}
