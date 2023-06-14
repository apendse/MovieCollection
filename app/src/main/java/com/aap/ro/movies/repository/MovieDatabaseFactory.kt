package com.aap.ro.movies.repository

import android.content.Context
import androidx.room.Room
import com.aap.ro.movies.room.MovieDatabase

object MovieDatabaseFactory {

    @Volatile private var INSTANCE: MovieDatabase? = null

    fun getMovieDatabase(context: Context): MovieDatabase {
        return INSTANCE ?: synchronized(this) { INSTANCE ?: initDb(context)}
    }

    private fun initDb(applicationContext: Context): MovieDatabase {
        return Room.databaseBuilder(applicationContext, MovieDatabase::class.java, "movie_database.db").createFromAsset("database/movie_database.db").build()
    }


}