package com.aap.ro.movies.data

data class AddMovieData(val name: String, val year: Int,
                        val genres: List<SelectableGenre>,
                        val selectedArtistList: List<SelectableArtist>,)
