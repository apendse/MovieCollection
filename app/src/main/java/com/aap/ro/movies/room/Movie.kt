package com.aap.ro.movies.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val action = 1
const val thriller = 2
const val drama = 4
const val war  = 8
const val fantasy = 16
const val romance = 32
const val adventure = 64
const val sciFi = 128
const val family = 256
const val crime = 512
const val comedy = 1024
const val western = 2048

@Entity(tableName = "movie")
data class Movie (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val movieName: String?,
    @ColumnInfo(name = "release_year") val releaseYear: Int?,
    @ColumnInfo(name = "genre") val genre: Int)