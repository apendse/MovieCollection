package com.aap.ro.movies.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryImpl.Companion.getGenreAsList
import com.aap.ro.movies.room.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieRepository: MovieRepository): ViewModel() {

    //@Inject
    //lateinit
//    val movieList: Flow<List<MovieVO>> = movieRepository.getMovieList().map {
//        getMovieVOList(it)
//    }

    init {
        Log.d("YYYY", "In")
    }

    private var _query: String = ""
    var query: String
        get() = _query
        set(value) {
            _query = value
            obtainMovieList(_query)
        }
    suspend fun loadDataIfNeeded() {
        withContext(Dispatchers.IO) {
            if (movieRepository.getMovieCount() == 0) {
                val populator = movieRepository.getMovieDatabasePopulator()
                populator.insertSampleMovies()
                populator.insertSampleArtists()
                populator.insertSampleMovieToArtist()
            }
        }
    }

    @VisibleForTesting
    val privateLoadingStateFlow : MutableStateFlow<Boolean> =  MutableStateFlow(true)
    val loadingState: Flow<Boolean>
        get() = privateLoadingStateFlow


    val movieListAsLiveData: LiveData<List<MovieVO>>
        get() = obtainMovieList(query).asLiveData()

    fun obtainMovieList(query: String): Flow<List<MovieVO>> {
        return movieRepository.getMovieList("%${query}%").map {
            privateLoadingStateFlow.emit(false)
            getMovieVOList(it)
        }
    }
    private fun getMovieVOList(input: List<Movie>): List<MovieVO> {
        return input.map {
            MovieVO(
                it.id,
                it.movieName ?: "",
                it.releaseYear ?: -1,
                getGenreAsList(it.genre),
                emptyList(),
                emptyList()
            )
        }
    }

    fun searchForMovies(query: String) =
         obtainMovieList(query)


}