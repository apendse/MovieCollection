package com.aap.ro.movies.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class, Artist::class, MovieToArtist::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun artistDao(): ArtistDao
    abstract fun movieToArtistDao(): MovieToArtistDao

}

