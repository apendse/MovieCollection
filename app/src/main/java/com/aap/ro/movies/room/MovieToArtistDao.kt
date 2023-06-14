package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieToArtistDao {
    @Insert
    fun insertAll(vararg movieToArtist: MovieToArtist)

    @Query("SELECT * FROM movie_to_artist WHERE movieId = :movieId")
    fun getArtists(movieId: Int): List<MovieToArtist>
}