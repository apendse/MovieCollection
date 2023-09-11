package com.aap.ro.movies.ui.addmovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aap.ro.movies.data.AddMovieData
import com.aap.ro.movies.data.ArtistVO
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.data.SelectableGenre
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryImpl
import com.aap.ro.movies.room.MovieToArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AddMovieData>(emptyMovie())

    val uiState: Flow<AddMovieData>
        get() = _uiState

    suspend fun createEmptyMovieData() {
        movieRepository.getAllArtists().map { artistList ->
            val selectableArtistList = artistList.map {
                SelectableArtist(
                    ArtistVO(
                    it.id,
                    it.name,
                    it.yearOfBirth,
                    it.placeOfBirth,
                    it.yearOfDeath), false
                )
            }.sortedBy { it.artistVO.name }

                AddMovieData("", -1,  emptyList<Genre>().toMutableList(),  Genre.values().toList().toMutableList(), selectableArtistList)

        }.collect {
            _uiState.value = it
        }

    }


    val genres: MutableList<SelectableGenre> = Genre.values().map { SelectableGenre(it, false) }.toMutableList()

    fun addMovie(title: String, genres: List<SelectableGenre>) {
        val oldMovie = _uiState.value
        val selectedGenres = genres.filter { it.selected }.map {
            it.genre
        }
        val updatedMovie = oldMovie.copy(name = title, year = oldMovie.year,  selectedGenres = oldMovie.selectedGenres)
        viewModelScope.launch(Dispatchers.IO) {
            //val id = movieRepository.getMovieCount()
            val movieIdForNewMovie = movieRepository.addMovie(
                MovieVO(
                    0,
                    updatedMovie.name,
                    updatedMovie.year,
                    selectedGenres )
            )
            Log.d("YYYY", "Movie id is $0 : return $movieIdForNewMovie")
            val artistsInMovie = updatedMovie.selectedArtistList.filter {
                it.selected
            }.map { MovieToArtist(null, movieIdForNewMovie.toInt(), it.artistVO.id, MovieRepositoryImpl.ACTOR) }
                .toTypedArray()
            movieRepository.addMovieArtists(artistsInMovie)
        }
    }

    fun changeArtistSelection(artist: SelectableArtist, isSelected: Boolean) {
        val addMovieData = _uiState.value
        val artists = addMovieData.selectedArtistList
        val artistFromList = artists.find { it.artistVO == artist.artistVO } ?: throw RuntimeException("Artist not found")
        if (artistFromList.selected == isSelected) {
            return
        }
        val updatedArtists = artists.map {
            if (it.artistVO == artist.artistVO) {
                SelectableArtist(it.artistVO, isSelected)
            } else {
                it
            }
        }
        //val d =
        Log.d("YYYY", "Updating the ui state value ")
        _uiState.value = addMovieData.copy(selectedArtistList = updatedArtists)
//        viewModelScope.launch {
//            _uiState.emit(d)
//        }
    }

    fun updateTitle(str: String) {
        val addMovieData = (_uiState.value)
        viewModelScope.launch {
            _uiState.emit(addMovieData.copy(name = str))
        }

    }

    fun setYear(year: Int) {
        val addMovieData = _uiState.value

        _uiState.value = addMovieData.copy(year = year)
    }

    private fun emptyMovie() = AddMovieData("", -1,  emptyList(), emptyList(), emptyList())

}

