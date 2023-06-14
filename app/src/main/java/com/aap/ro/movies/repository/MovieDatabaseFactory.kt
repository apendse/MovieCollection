package com.aap.ro.movies.repository

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.aap.ro.movies.room.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//object MovieDatabaseFactory {
//
//    @Volatile private var INSTANCE: MovieDatabase? = null
//
//    fun getMovieDatabase(context: Context): MovieDatabase {
//        return INSTANCE ?: synchronized(this) { INSTANCE ?: initDb(context)}
//    }
//
//    private fun initDb(applicationContext: Context): MovieDatabase {
//        return Room.databaseBuilder(applicationContext, MovieDatabase::class.java, "movie_database.db").createFromAsset("database/movie_database.db").build()
//    }
//
//
//}



@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {

    @Provides
    @Singleton
    fun provideDb(application: Application): MovieDatabase {
        Log.d("YYYY", "in provideDb")
        return Room.databaseBuilder(application.applicationContext, MovieDatabase::class.java, "movie_database.db").createFromAsset("database/movie_database.db").build()
    }
}