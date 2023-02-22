package com.example.myego.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myego.R
import com.example.myego.databinding.ItemlistPokemonsBinding
import com.example.myego.datamodel.PokemonOverview
import com.example.myego.datamodel.PokemonPhoto
import com.example.myego.datamodel.Sprites
import com.example.myego.extensions.getPokemonIdFromUrl

class PokemonsPagingDataAdapter (
    val actionItemClicked: (String, Int) -> Unit
) : PagingDataAdapter<PokemonOverview, PokemonsPagingDataAdapter.PokemonViewHolder>(PokemonsDiffUtilItemCallback()) {

    inner class PokemonViewHolder(val viewBinding: ItemlistPokemonsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        lateinit var currentPokemonId: String

        fun setCurrentPokemonId(itemData: PokemonOverview) {
            val pokemonDetailsUrl: String = itemData.urlPokemonDetails.orEmpty()
            currentPokemonId = pokemonDetailsUrl.getPokemonIdFromUrl()
        }

        fun expandCollapse(itemData: PokemonOverview) {

            if(!itemData.uiIsExpanded){
                viewBinding.ivLogoPokemon.setBackgroundResource(R.drawable.pokemon_closed)
                viewBinding.ivArrowExpandCollapse.setBackgroundResource(R.drawable.arrow_up_pokemon)
                viewBinding.tvActionDetails.text = "Explore"
                viewBinding.progressBarPokemonDetails.visibility = View.GONE
                viewBinding.tvBaseExperience.visibility = View.GONE
                viewBinding.rvPokemonPhotos.visibility = View.GONE
                viewBinding.tvId.visibility = View.GONE
                viewBinding.tvOrder.visibility = View.GONE
                viewBinding.tvHeight.visibility = View.GONE
                viewBinding.tvWeight.visibility = View.GONE
            } else {
                viewBinding.ivLogoPokemon.setBackgroundResource(R.drawable.pokemon_openned)
                viewBinding.ivArrowExpandCollapse.setBackgroundResource(R.drawable.arrow_down_pokemon)
                viewBinding.tvActionDetails.text = "Collapse"
                viewBinding.progressBarPokemonDetails.visibility = View.GONE
                viewBinding.tvBaseExperience.visibility = View.VISIBLE
                viewBinding.rvPokemonPhotos.visibility = View.VISIBLE
                viewBinding.tvId.visibility = View.VISIBLE
                viewBinding.tvOrder.visibility = View.VISIBLE
                viewBinding.tvHeight.visibility = View.VISIBLE
                viewBinding.tvWeight.visibility = View.VISIBLE
            }
        }

        fun bind(itemData: PokemonOverview) {
            viewBinding.tvName.text = itemData.name.orEmpty().uppercase()
            viewBinding.tvBaseExperience.text = "Base Experience: ${itemData.uiBaseExperience?:-1}"
            viewBinding.tvId.text = "Id: ${itemData.uiPokemonId?:-1}"
            viewBinding.tvOrder.text = "Order: ${itemData.uiOrder?:-1}"
            viewBinding.tvHeight.text = "Height: ${itemData.uiHeight?:-1}"
            viewBinding.tvWeight.text = "Weight: ${itemData.uiWeight?:-1}"

            setupPokemonPhotosRecyclerView(viewBinding.rvPokemonPhotos, itemData.uiSprites)
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


        fun setOnItemClicked(itemData: PokemonOverview, dataPosition: Int) {
            viewBinding.root.setOnClickListener {
                viewBinding.progressBarPokemonDetails.visibility = View.VISIBLE
                actionItemClicked(currentPokemonId, dataPosition)
            }
        }

        fun setPokemonDetailsProgressBar(pokemonOverview: PokemonOverview) {
            if(pokemonOverview.uiDataIsLoading){
                viewBinding.progressBarPokemonDetails.visibility = View.VISIBLE
            } else viewBinding.progressBarPokemonDetails.visibility = View.GONE
        }
    }

    class PokemonsDiffUtilItemCallback : DiffUtil.ItemCallback<PokemonOverview>() {
        override fun areItemsTheSame(
            oldItem: PokemonOverview,
            newItem: PokemonOverview
        ): Boolean {
            return newItem.hashCode() == oldItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: PokemonOverview,
            newItem: PokemonOverview
        ): Boolean {
            return oldItem.urlPokemonDetails.equals(newItem.urlPokemonDetails)
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
        val currentDataItem: PokemonOverview? = this.getItem(position)
        currentDataItem?.let { pokemonOverview ->
            holder.setCurrentPokemonId(pokemonOverview)
            holder.expandCollapse(pokemonOverview)
            holder.bind(pokemonOverview)
            holder.setOnItemClicked(pokemonOverview, position)
            holder.setPokemonDetailsProgressBar(pokemonOverview)
        }
    }

}
