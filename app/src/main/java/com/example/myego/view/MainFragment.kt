package com.example.myego.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myego.R
import com.example.myego.adapter.PokemonsPagingDataAdapter
import com.example.myego.viewmodel.MainFragmentViewModel
import com.example.myego.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    lateinit var viewBinding: FragmentMainBinding
    val viewModel: MainFragmentViewModel by viewModels()
    lateinit var pokemonsPagingDataAdapter: PokemonsPagingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(inflater)

        setHasOptionsMenu(true)

        return viewBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fetchData -> {
                viewBinding.rvPokemons.visibility = View.GONE
                viewBinding.progressBar.visibility = View.VISIBLE
                viewBinding.tvResults.text = ""
                viewModel.fetchApiAndUpdatePagingData()
                true
            }
            R.id.reset -> {
                viewBinding.progressBar.visibility = View.GONE
                viewBinding.tvResults.visibility = View.GONE
//                pokemonPagingDataAdapter.refresh()
                true
            }
            else -> false
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokemonsPagingDataAdapter = PokemonsPagingDataAdapter(
            viewModel,
            this
        )

        with(viewBinding.rvPokemons) {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(false)  // IMPORTANT we have to make it FALSE not like Normal RecyclerView!!!

            adapter = pokemonsPagingDataAdapter
        }



        viewModel.pokemonPagingDataLiveData.observe(
            viewLifecycleOwner,
            { pagingData ->
                viewBinding.progressBar.visibility = View.GONE
                viewBinding.rvPokemons.visibility = View.VISIBLE
                viewBinding.tvResults.visibility = View.VISIBLE
                viewBinding.tvResults.text =
                    viewBinding.tvResults.text.toString().plus("PagingData Here")

                pagingData?.let {
                    pokemonsPagingDataAdapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        pagingData
                    )
                }
            }
        )

    }


}