package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Insert
    fun insertAll(vararg artists: Artist)
    @Query("SELECT * FROM Artist")
    fun getAllArtists(): Flow<List<Artist>>

    @Query("SELECT * FROM Artist WHERE id = :id")
    fun getArtist(id: Int): Artist
}