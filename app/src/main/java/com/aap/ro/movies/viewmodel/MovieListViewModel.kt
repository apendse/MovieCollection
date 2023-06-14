package com.aap.ro.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.aap.ro.movies.MovieApplication
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.action
import com.aap.ro.movies.room.adventure
import com.aap.ro.movies.room.comedy
import com.aap.ro.movies.room.crime
import com.aap.ro.movies.room.drama
import com.aap.ro.movies.room.family
import com.aap.ro.movies.room.fantasy
import com.aap.ro.movies.room.romance
import com.aap.ro.movies.room.sciFi
import com.aap.ro.movies.room.thriller
import com.aap.ro.movies.room.war
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieListViewModel(private val movieRepository: MovieRepository): ViewModel() {

//    val movieList: Flow<List<MovieVO>> = movieRepository.getMovieList().map {
//        getMovieVOList(it)
//    }

    suspend fun loadDataIfNeeded() {
        withContext(Dispatchers.IO) {
            if (movieRepository.getMovieCount() == 0) {
                movieRepository.insertSampleMovies()
                movieRepository.insertSampleArtists()
                movieRepository.insertSampleMovieToArtist()
            }
        }
    }

    fun obtainMovieList(): Flow<List<MovieVO>> {
        return movieRepository.getMovieList().map {
            getMovieVOList(it)
        }
    }
    private fun getMovieVOList(input: List<Movie>): List<MovieVO> {
        return input.map {
            MovieVO(it.id, it.movieName ?: "", it.releaseYear ?: -1, getGenreAsList(it.genre), emptyList(), emptyList())
        }
    }

    private val genreList = listOf(action to Genre.ACTION,
        thriller to Genre.THRILLER,
        drama to Genre.DRAMA,
        war to Genre.WAR,
        fantasy to Genre.FANTASY,
        romance to Genre.ROMANCE,
        adventure to Genre.ADVENTURE,
        sciFi to Genre.SCI_FI,
        family to Genre.FAMILY,
        crime to Genre.CRIME,
        comedy to Genre.COMEDY,)

    private fun getGenreAsList(g: Int): List<Genre> {
        val ret = arrayListOf<Genre>()
        genreList.forEach {
            if (it.first and g != 0) {
                ret.add(it.second)
            }
        }
        return ret
    }
    
    companion object {

        val Factory:
                ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                try {
                    return MovieListViewModel(
                        (application as MovieApplication).movieRepository,

                        ) as T
                } catch (e: Exception) {
                    Log.d("YYY", "Execption $e")
                    throw e
                }
            }
        }
    }
}