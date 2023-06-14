package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArtistDao {

    @Insert
    fun insertAll(vararg artists: Artist)

    @Query("SELECT * FROM Artist WHERE id = :id")
    fun getArtist(id: Int): Artist
}