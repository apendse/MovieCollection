package com.aap.ro.movies.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Artist(@PrimaryKey val id: Int, @ColumnInfo(name = "actor_name") val name: String,
                  @ColumnInfo(name = "birth_year") val yearOfBirth: Int?,
                  @ColumnInfo(name = "place_of_birth") val placeOfBirth: String?)
