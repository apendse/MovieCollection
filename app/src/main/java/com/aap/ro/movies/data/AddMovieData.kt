package com.aap.ro.movies.data

data class AddMovieData(val name: String, val year: Int,
                        val selectedGenres: List<Genre>, val notSelectedGenres: List<Genre>,
                        val selectedArtistList: List<SelectableArtist>,)
