package com.example.myego.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myego.api.RetrofitApi
import com.example.myego.datamodel.PokemonDetails
import com.example.myego.datamodel.PokemonOverview
import com.example.myego.datamodel.Pokemons
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val retrofitApi: RetrofitApi
) {

    val pokemonDetails: MutableStateFlow<BackendResult> =
        MutableStateFlow<BackendResult>(BackendNotCalledYet)

    fun getPagingDataFlow() : Flow<PagingData<PokemonOverview>> {

        val pagingConfig = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        )

        val lambdaPagingSourceToCheckSuspend: () -> PokemonPagingSource =
            { PokemonPagingSource(retrofitApi) }

        // Pager is reactive stream of PagingData
        val pokemonPager: Pager<Int, PokemonOverview> = Pager<Int, PokemonOverview>(
            config = pagingConfig,
            pagingSourceFactory = lambdaPagingSourceToCheckSuspend
        )

        val flowOfPokemonsPagingData: Flow<PagingData<PokemonOverview>> = pokemonPager.flow

        return flowOfPokemonsPagingData
    }


    suspend fun fetchPokemonDetails(pokemonId: String){

        try {
            val response: Response<PokemonDetails> = retrofitApi.getPokemonDetails(pokemonId)

            // Simulate Late
            pokemonDetails.value = BackendLoading
            delay(5_000)

            if (response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: PokemonDetails? = response.body()

                if (responseBody != null){
                    pokemonDetails.value = BackendSuccess(responseBody)
                    Log.d("retrofitCall", responseBody.toString())

                } else{
                    pokemonDetails.value = BackendFailure("No Pokemons now")
                    Log.d("retrofitCall", "No Pokemons now")
                }
            }

        } catch (e: IOException) {
            Log.d("retrofitCall", "No Internet!")
            pokemonDetails.value = BackendFailure("No Internet!")

        } catch (exception: HttpException) {
            Log.d("retrofitCall", "Server Broken!")
            pokemonDetails.value = BackendFailure("Server Broken!")
        }
    }

}



// Backend Status
sealed class BackendResult
data class BackendSuccess(val pokemonDetails: PokemonDetails): BackendResult()
data class BackendFailure(val message: String): BackendResult()
object BackendNotCalledYet: BackendResult()
object BackendLoading: BackendResult()
