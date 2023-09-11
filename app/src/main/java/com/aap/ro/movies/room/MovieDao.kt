package com.aap.ro.movies.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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


    @Query("SELECT * FROM Movie where name LIKE :query or release_year = :query")
    fun getMatchingMovies(query: String): Flow<List<Movie>>



    @Query("SELECT COUNT(*) from Movie")
    fun getMovieCount(): Int


    @Query("SELECT * from movie where id = :movieIdParam")
    fun getMovieDetails(movieIdParam: Int): Flow<Movie>


    @Query("SELECT * from movie where id = :movieIdParam")
    fun getMovieDetailsWithoutFlow(movieIdParam: Int): Movie

    @Insert
    fun insertAll(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Long

    @Delete
    suspend fun deleteMovie(movie: Movie)


}