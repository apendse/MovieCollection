package com.aap.ro.movies

//import com.aap.ro.movies.repository.MovieRepositoryFactory
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication: Application() {
    //lateinit var movieRepository: MovieRepository

    override fun onCreate() {

        super.onCreate()
        //movieRepository = MovieRepositoryFactory.getMovieRepository(MovieDatabaseFactory.getMovieDatabase(this))
    }
}