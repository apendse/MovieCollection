package com.aap.ro.movies.repository

import com.aap.ro.movies.room.MovieDatabase

object MovieRepositoryFactory {
    @Volatile private var INSTANCE: MovieRepository? = null
    fun getMovieRepository(database: MovieDatabase): MovieRepository {
        return INSTANCE ?: synchronized(this) { INSTANCE ?: MovieRepository(database).apply {
            INSTANCE = this
        }
        }
    }
}