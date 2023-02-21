package com.example.myego.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myego.api.RetrofitApi
import com.example.myego.datamodel.PokemonDetails
import com.example.myego.datamodel.PokemonOverview
import com.example.myego.datamodel.Pokemons
import com.example.myego.extensions.getPokemonIdFromUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class PokemonPagingSource(
    private val retrofitApi: RetrofitApi
)  : PagingSource<Int, PokemonOverview>() {

    // called every time we load a new page
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonOverview> {
        val currentPageIndex: Int = params.key ?: 0 // We get from params
        val numPagesToLoad: Int = params.loadSize  // We get from params

        try {
            val response: Response<Pokemons> = retrofitApi.getAllPokemons(currentPageIndex, numPagesToLoad)

            // Simulate Late
            delay(5_000)

            if (response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: Pokemons? = response.body()

                if (responseBody != null){

                    val listOfPokemons: List<PokemonOverview>? = responseBody.listOfPokemonOverviews

                    if(listOfPokemons != null){

                        val prevKey = if (currentPageIndex == 1) null else currentPageIndex - 1
                        val nextKey = if (listOfPokemons.isNullOrEmpty()) null else (currentPageIndex + 1)

                        return LoadResult.Page<Int, PokemonOverview>(listOfPokemons, prevKey, nextKey)

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

    override fun getRefreshKey(state: PagingState<Int, PokemonOverview>): Int? {
        TODO("Not yet implemented")
    }

}

enum class ResponseMessage(val message: String) {
    SUCCESS_LIST_NOT_EMPTY_NOT_ENDED("Success, list has not ended and has Items"),
    LIST_OF_RESULTS_ENDED("The List has ended !"),
    ERROR_NO_INTERNET("Error: Check Internet connection !"),
    NO_RESULTS("No results for your search, try something else."),
    ERROR_SERVER_BROKEN("Something bad happened, server is broken")
}