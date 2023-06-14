package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

const val ROLE_DIRECTOR = "director"
const val ROLE_ACTOR = "actor"
const val ROLE_PRODUCER = "producer"
const val MUSIC_COMPOSER = "music_composer"

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie")
    fun getMovieList(): Flow<List<Movie>>

    @Query("SELECT COUNT(*) from Movie")
    fun getMovieCount(): Int

    @Insert
    fun insertAll(vararg movies: Movie)

}