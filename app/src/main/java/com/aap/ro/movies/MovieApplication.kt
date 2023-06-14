package com.aap.ro.movies

import android.app.Application
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.coroutineScope
import com.aap.ro.movies.repository.MovieDatabaseFactory
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryFactory
import com.aap.ro.movies.room.MovieDatabase

class MovieApplication: Application() {
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {

        super.onCreate()
        movieRepository = MovieRepositoryFactory.getMovieRepository(MovieDatabaseFactory.getMovieDatabase(this))
    }
}