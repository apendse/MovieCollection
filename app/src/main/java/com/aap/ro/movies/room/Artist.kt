package com.aap.ro.movies.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class Artist(@PrimaryKey val id: Int, @ColumnInfo(name = "artist_name") val name: String,
                  @ColumnInfo(name = "year_of_birth") val yearOfBirth: Int?,
                  @ColumnInfo(name = "place_of_birth") val placeOfBirth: String?,
                  @ColumnInfo("year_of_death") val yearOfDeath: Int?)


data class ArtistWithRole( val id: Int,  @ColumnInfo(name = "artist_name") val name: String,
                           @ColumnInfo(name = "year_of_birth") val yearOfBirth: Int?,
                           @ColumnInfo(name = "place_of_birth") val placeOfBirth: String?,
                           @ColumnInfo("year_of_death") val yearOfDeath: Int?, val role: String?)

