package com.aap.ro.movies.viewmodel

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

    suspend fun createMovieData(unused: Int) {
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

                AddMovieData("", -1,  Genre.values().toList().map { SelectableGenre(it, false) }, selectableArtistList)

        }.collect {
            Log.d("YYYY", "Setting uistate 1")
            _uiState.value = it
        }

    }

    fun addMovie(title: String) {
        val addMovieData = _uiState.value
        val updatedMovie = addMovieData.copy(name = title)
        viewModelScope.launch(Dispatchers.IO) {
            val id = movieRepository.addMovie(
                MovieVO(
                    -1,
                    updatedMovie.name,
                    updatedMovie.year,
                    updatedMovie.genres.filter { it.selected }.map { it.genre })
            )
            val artistsInMovie = updatedMovie.selectedArtistList.filter {
                it.selected
            }.map { MovieToArtist(null, id.toInt(), it.artistVO.id, MovieRepositoryImpl.ACTOR) }
                .toTypedArray()
            movieRepository.addMovieArtists(artistsInMovie)
        }
    }

    fun changeArtistSelection(artist: SelectableArtist) {
        val addMovieData = _uiState.value
        val artists = addMovieData.selectedArtistList
        val updatedArtists = artists.map {
            if (it.artistVO == artist.artistVO) {
                SelectableArtist(it.artistVO, !it.selected)
            } else {
                it
            }
        }
        val d = addMovieData.copy(selectedArtistList = updatedArtists)
        viewModelScope.launch {
            _uiState.emit(d)
        }
    }

    fun updateTitle(str: String) {
        val addMovieData = (_uiState.value)
        viewModelScope.launch {
            _uiState.emit(addMovieData.copy(name = str))
        }

    }

    fun onGenreClicked(selectableGenre: SelectableGenre) {
        val addMovieData = (_uiState.value)
        val genres = addMovieData.genres.toMutableList()
        val updated = genres.map {
            if (it == selectableGenre) {
                SelectableGenre(it.genre, !it.selected)
            } else {
                it
            }
        }
        _uiState.value = addMovieData.copy(genres = updated)
    }

    fun setYear(year: Int) {
        val addMovieData = _uiState.value

        _uiState.value = addMovieData.copy(year = year)
    }

    private fun emptyMovie() = AddMovieData("", -1,  emptyList(), emptyList())




}

