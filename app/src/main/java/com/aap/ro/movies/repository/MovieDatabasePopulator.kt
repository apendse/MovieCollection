package com.aap.ro.movies.repository

interface MovieDatabasePopulator {
    fun insertSampleMovies()
    fun insertSampleArtists()
    fun insertSampleMovieToArtist()
}