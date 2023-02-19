package com.example.myego.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myego.api.RetrofitApi
import com.example.myego.datamodel.Pokemons
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class PokemonPagingSource(
    private val retrofitApi: RetrofitApi
)  : PagingSource<Int, Pokemons.PokemonOverview>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemons.PokemonOverview> {
        val currentPageIndex: Int = params.key ?: 0 // We get from params
        val numPagesToLoad: Int = params.loadSize  // We get from params

        try {
            val response: Response<Pokemons> = retrofitApi.getAllPokemons(currentPageIndex, numPagesToLoad)

            // Simulate Late
            delay(5_000)

            if (response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: Pokemons? = response.body()

                if (responseBody != null){

                    if(responseBody.listOfPokemonOverviews != null){

                        val listOfPokemons: List<Pokemons.PokemonOverview> = responseBody.listOfPokemonOverviews.toList()

                        val prevKey = if (currentPageIndex == 1) null else currentPageIndex - 1
                        val nextKey = if (listOfPokemons.isNullOrEmpty()) null else (currentPageIndex + 1)

                        return LoadResult.Page<Int, Pokemons.PokemonOverview>(listOfPokemons, prevKey, nextKey)
                    } else return LoadResult.Error(Throwable("No Pokemons now"))

                } else return LoadResult.Error(Throwable("No Pokemons now"))

            } else return LoadResult.Error(Throwable("Other backend problem"))

        } catch (e: IOException) {
            Log.d("retrofitCall", "No Internet!")
            return LoadResult.Error(Throwable("No Internet!"))

        } catch (exception: HttpException) {
            Log.d("retrofitCall", "Server Broken!")
            return LoadResult.Error(Throwable("Server Broken!"))

        }

    }



    override fun getRefreshKey(state: PagingState<Int, Pokemons.PokemonOverview>): Int? {
        TODO("Not yet implemented")
    }

}