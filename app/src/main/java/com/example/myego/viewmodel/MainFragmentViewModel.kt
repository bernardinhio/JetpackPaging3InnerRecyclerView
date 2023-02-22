package com.example.myego.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myego.data.*
import com.example.myego.datamodel.PokemonDetails
import com.example.myego.datamodel.PokemonOverview
import com.example.myego.datamodel.Pokemons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val pokemonPagingDataLiveData = MutableLiveData<PagingData<PokemonOverview>>()

    val pokemonDetailsLiveData = MutableLiveData<PokemonDetails>()
    val isPokemonDetailsProgressBarVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isPokemonDetailsErrorToastVisible: MutableLiveData<Boolean> = MutableLiveData()
    val pokemonDetailsErrorMessage: MutableLiveData<String> = MutableLiveData()

    fun fetchApiAndUpdatePagingData() {

        val flow: Flow<PagingData<PokemonOverview>> =
            repository.getPagingDataFlow()
                .cachedIn(viewModelScope)  // We are NOT allowed to call twice the same call to PagingData

        viewModelScope.launch {
            flow.collect { pagingData ->
                pokemonPagingDataLiveData.value = pagingData
            }
        }
    }

    fun fetchPokemonDetails(pokemonId: String){
        viewModelScope.launch {

            repository.fetchPokemonDetails(pokemonId)

            repository.pokemonDetails.collect { backendResult ->
                when (backendResult) {
                    is BackendNotCalledYet -> {
                        isPokemonDetailsProgressBarVisible.value = false
                        isPokemonDetailsErrorToastVisible.value = false
                    }
                    is BackendSuccess -> {
                        pokemonDetailsLiveData.value = backendResult.pokemonDetails
                        isPokemonDetailsProgressBarVisible.value = false
                        isPokemonDetailsErrorToastVisible.value = false
                    }
                    is BackendFailure -> {
                        isPokemonDetailsProgressBarVisible.value = false
                        isPokemonDetailsErrorToastVisible.value = true
                        pokemonDetailsErrorMessage.value = backendResult.message
                    }
                    is BackendLoading -> {
                        isPokemonDetailsProgressBarVisible.value = true
                        isPokemonDetailsErrorToastVisible.value = false
                    }

                }
            }

        }
    }

}