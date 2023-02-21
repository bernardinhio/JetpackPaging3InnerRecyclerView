package com.example.myego.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myego.data.ResponseMessage
import com.example.myego.databinding.ItemlistFooterPokemonsBinding

class PokemonsLoadStateAdapter(private val actionLambdaForOnClick: () -> Unit) :
    LoadStateAdapter<PokemonsLoadStateAdapter.PokemonsLoadStateAdapterViewHolder>() {

    var roundLoadStateLoading = 0
    var roundLoadStateError = 0

    inner class PokemonsLoadStateAdapterViewHolder(private val binding: ItemlistFooterPokemonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * LoadState has 3 states:
         *
         * 1. LoadState.Loading
         * >> When the PagingSource is actively loading Items
         *
         * 2. LoadState.NotLoading
         * >> Means there is no active load operation and no errors.
         * This happens when all the Items are used
         *
         * 3. LoadState.Error
         * > Means then there is an error.
         */
        fun bind(loadState: LoadState) {
            when (loadState) {

                is LoadState.Loading -> {

                    // Just for me
                    roundLoadStateLoading++
                    Log.d("pagingLog", "round LoadState.Loading: $roundLoadStateLoading")

                    with(binding) {
                        progressBar.visibility = View.VISIBLE
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = "Loading Pokemons..."
                        tvMessage.setTextColor(Color.RED)
                        root.background = ColorDrawable(Color.YELLOW)
                        btnLoadStateAction.visibility = View.GONE
                    }
                }

                is LoadState.Error -> {

                    // Just for me
                    roundLoadStateError++
                    Log.d("pagingLog", "round LoadState Error: $roundLoadStateError")

                    with(binding)  {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = loadState.error.message
                        tvMessage.setTextColor(Color.WHITE)
                    }

                    when (loadState.error.message) {

                        ResponseMessage.LIST_OF_RESULTS_ENDED.message -> {
                            with(binding) {
                                progressBar.visibility = View.GONE
                                root.background = ColorDrawable(Color.BLUE)
                                btnLoadStateAction.visibility = View.GONE
                            }
                        }

                        ResponseMessage.NO_RESULTS.message -> {
                            with(binding) {
                                progressBar.visibility = View.GONE
                                root.background = ColorDrawable(Color.BLUE)
                                btnLoadStateAction.visibility = View.GONE
                            }
                        }

                        ResponseMessage.ERROR_NO_INTERNET.message -> {
                            with(binding) {
                                progressBar.visibility = View.VISIBLE
                                root.background = ColorDrawable(Color.RED)
                                btnLoadStateAction.visibility = View.VISIBLE
                                btnLoadStateAction.text = "Retry!"
                            }
                        }

                        ResponseMessage.ERROR_SERVER_BROKEN.message -> {
                            with(binding) {
                                progressBar.visibility = View.VISIBLE
                                root.background = ColorDrawable(Color.RED)
                                btnLoadStateAction.visibility = View.VISIBLE
                                btnLoadStateAction.text = "Retry!"
                            }
                        }

                    }
                }
            }
        }


        /**
         * It is better to create the onClickListeners of the Buttons INSIDE
         * the ViewHolder not inside the onBindingView() fun of the Adapter, it
         * is preferable because the onBindingView() will be repeated for EVERY
         * Item added BUT the ViewHolder will be created ONLY FEW TIMES in a
         * RecyclerView depending on the height of the Item and how much Items
         * are created at once when scrolling
         *
         * What we want our click action to do?
         * We can pass an actionLambda to our LoadStateAdapter, and when click
         * on the Button that we designed in our Footer, we can just execute
         * this action Lamda !!!
         *
         * This is why we can pass a Lambda to the constructor of our LoadStateAdapter()
         *
         */
        init {
            binding.btnLoadStateAction.setOnClickListener {
                actionLambdaForOnClick.invoke()
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PokemonsLoadStateAdapterViewHolder {
        val binding = ItemlistFooterPokemonsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonsLoadStateAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonsLoadStateAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}