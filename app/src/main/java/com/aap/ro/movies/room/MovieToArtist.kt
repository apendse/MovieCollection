package com.aap.ro.movies.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_to_artist",
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("artistId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(entity = Movie::class, parentColumns = arrayOf("id"), childColumns = arrayOf("movieId"),
    onDelete = ForeignKey.CASCADE)           ]
)data class MovieToArtist(@PrimaryKey val id: Int?, val movieId: Int, val artistId: Int, val role: String)