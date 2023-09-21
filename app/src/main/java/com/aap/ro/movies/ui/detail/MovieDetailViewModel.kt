package com.aap.ro.movies.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aap.ro.movies.data.ArtistVO
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryImpl.Companion.ACTOR
import com.aap.ro.movies.repository.MovieRepositoryImpl.Companion.DIRECTOR
import com.aap.ro.movies.repository.MovieRepositoryImpl.Companion.getGenreAsList
import com.aap.ro.movies.room.ArtistWithRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val movieRepository: MovieRepository): ViewModel() {
    private val _uiState = MutableStateFlow(emptyMovie())

    val uiState: Flow<MovieVO>
        get() = _uiState

    private fun emptyMovie() = MovieVO(-1, "", -1, emptyList(), emptyList(), emptyList())

    fun getMovieName() = _uiState.value.name

    fun deleteMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.deleteMovie(movieId)
        }
    }

    suspend fun getMovieDetail(movieId: Int) {

        movieRepository.getMovieDetail(movieId).collect { movie ->
            movieRepository.getMovieArtists(movieId).collect() { list ->

                val groups = list.groupBy { it.role }
                val actors = getArtistGroup(groups, ACTOR)
                val directors = getArtistGroup(groups, DIRECTOR)
                val movieVO = MovieVO(
                    movie.id,
                    movie.movieName ?: "",
                    movie.releaseYear ?: -1,
                    getGenreAsList(movie.genre),
                    directors,
                    actors,
                    moviePoster = movie.poster
                )
                _uiState.emit(movieVO)
            }
        }
    }

    private fun getArtistGroup(groups: Map<String?, List<ArtistWithRole>?>, group: String) = groups[group]?.map { ArtistVO(it.id, it.name, it.yearOfBirth, it.placeOfBirth, it.yearOfDeath) } ?: emptyList()


}