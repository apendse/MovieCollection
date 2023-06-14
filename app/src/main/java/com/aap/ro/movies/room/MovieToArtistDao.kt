package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieToArtistDao {
    @Insert
    fun insertAll(vararg movieToArtist: MovieToArtist)

    @Query("SELECT artist.id, artist.artist_name, artist.year_of_birth, artist.place_of_birth, artist.year_of_death, role FROM movie_to_artist INNER JOIN artist ON movie_to_artist.artistId = artist.id WHERE movieId = :movieId")
    fun getArtistsForMovie(movieId: Int): Flow<List<ArtistWithRole>>

}