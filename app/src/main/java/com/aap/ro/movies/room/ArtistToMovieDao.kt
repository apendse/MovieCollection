package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Gets tha movies that match an artist
 */
@Dao
interface ArtistToMovieDao {
    @Query("SELECT movieId, movie.name, movie.release_year FROM movie_to_artist INNER JOIN movie ON movie_to_artist.movieId = movie.id WHERE artistId = :artistId")
    fun getMoviesForArtist(artistId: Int): Flow<List<Movie>>
}