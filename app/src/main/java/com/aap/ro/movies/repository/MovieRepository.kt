package com.aap.ro.movies.repository

import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.room.Artist
import com.aap.ro.movies.room.ArtistWithRole
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.MovieToArtist
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovieList(query: String) : Flow<List<Movie>>

    fun getMovieCount(): Int

    fun getAllArtists() : Flow<List<Artist>>
    fun getMovieDetail(id: Int) : Flow<Movie>
    fun getMovieArtists(movieId: Int) : Flow<List<ArtistWithRole>>

    fun addMovie(movievo: MovieVO): Long

    fun addMovieArtists(movieArtists: Array<MovieToArtist>)

    suspend fun deleteMovie(id: Int)

    fun getMovieDatabasePopulator(): MovieDatabasePopulator
}