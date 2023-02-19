package com.example.myego.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.myego.R
import com.example.myego.databinding.ItemlistPokemonPhotoBinding
import com.example.myego.datamodel.PokemonPhoto

class PokemonPhotosAdapter:
    ListAdapter<PokemonPhoto, PokemonPhotosAdapter.MyViewHolder>(PhotosDiffUtilItemCallback()) {

    class MyViewHolder(private val itemRecyclerViewBinding: ItemlistPokemonPhotoBinding) :
        RecyclerView.ViewHolder(itemRecyclerViewBinding.root) {
        fun bind(pokemonPhoto: PokemonPhoto) {
            itemRecyclerViewBinding.tvPokemonPhotoName.text = pokemonPhoto.type

            Glide.with(itemRecyclerViewBinding.root)
                .load(pokemonPhoto.url)
                .fitCenter()
                .override(390)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.no_image_available)
                .into(itemRecyclerViewBinding.ivPokemonPhoto)

        }
    }

    // we can also create an anonymous class Object inside the companion Object
    class PhotosDiffUtilItemCallback : DiffUtil.ItemCallback<PokemonPhoto>() {
        override fun areItemsTheSame(oldItem: PokemonPhoto, newItem: PokemonPhoto): Boolean {
            return newItem.hashCode() == oldItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: PokemonPhoto, newItem: PokemonPhoto): Boolean {
            return newItem == oldItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            // to put  , parent, false  makes the RecyclerView match_parent  in width
            ItemlistPokemonPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

}