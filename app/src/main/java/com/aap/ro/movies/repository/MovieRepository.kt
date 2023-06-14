package com.aap.ro.movies.repository

import androidx.annotation.VisibleForTesting
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.room.Artist
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.MovieDatabase
import com.aap.ro.movies.room.action
import com.aap.ro.movies.room.adventure
import com.aap.ro.movies.room.comedy
import com.aap.ro.movies.room.crime
import com.aap.ro.movies.room.drama
import com.aap.ro.movies.room.family
import com.aap.ro.movies.room.fantasy
import com.aap.ro.movies.room.romance
import com.aap.ro.movies.room.sciFi
import com.aap.ro.movies.room.thriller
import com.aap.ro.movies.room.war
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MovieRepository(private val database: MovieDatabase) {

    fun getMovieList() = database.movieDao().getMovieList()

    fun getMovieCount() = database.movieDao().getMovieCount()
    @VisibleForTesting
    fun getGenre(mask: Int): List<Genre> {
        return emptyList()
    }




    fun insertSampleMovieToArtist() {

    }

    fun insertSampleMovies() {


        val list = arrayOf(
            Movie(1, "Die Hard", 1989, action + thriller),
            Movie(2, "Saving Private Ryan ", 1998, drama + war),
            Movie(3, "Ghost", 1999, drama + fantasy + romance),
            Movie(4, "Indiana Jones and the Last Crusade", 1989, action + adventure + fantasy),
            Movie(5, "Jurassic Park", 1993, action + adventure + sciFi),
            Movie(6, "Goldfinger", 1964, action + adventure + thriller),
            Movie(7, "Sound of Music", 1965, drama + family),
            Movie(8, "Pulp Fiction", 1994, drama + crime),
            Movie(9, "Coming to America", 1988, comedy + romance),
            Movie(10, "Titanic", 1997, drama + romance),
        )

        database.movieDao().insertAll(*list)
    }
    fun insertSampleArtists() {
        var i = 1
        val list = arrayOf(
            Artist(i++, "Bruce Willis", 1955, "West Germany", null),
            Artist(i++, "Alan Rickman", 1946, "UK", 2016),
            Artist(i++, "Bonnie Bedelia", 1948, "New York, NY", null),
            Artist(i++, "Steven Spielberg", 1946, "Cincinnati, OH",null),
            Artist(i++, "Tom Hanks", 1956, "Concord, CA", null),
            Artist(i++, "Matt Damon", 1970, "Cambridge, MA", null),
            Artist(i++, "Tom Sizemore", 1961, "Detroit, MI", 2023),
            Artist(i++, "Jerry Zucker", 1965, "Homestead, FL", null),
            Artist(i++, "Patric Swayze", 1952, "Houston, TX", 2009),
            Artist(i++, "Demi Moore", 1962, "Roswell, NM", null),
            Artist(i++, "Whoopi Goldberg", 1955, "New York, NY", null),
            Artist(i++, "Harrison Ford", 1942, "Chicago, IL", null),
            Artist(i++, "Sean Connery", 1930, "Scotland", 2020),
            Artist(i++, "Alison Doody", 1966, "Ireland", null),
            Artist(i++, "Denholm Elliot", 1922, "UK", 1922),
            Artist(i++, "Sam Neil", 1947, "Northern Ireland", null),
            Artist(i++, "Laura Dern", 1967, "Los Angeles, CA", null),
            Artist(i++, "Jeff Goldblum", 1952, "West Homestead, PA", null),
            Artist(i++, "Samuel L. Jackson", 1948, "Washington DC", null),
            Artist(i++, "Gert Fröbe", 1913, "Germany", 1988),
            Artist(i++, "Honor Blackman", 1925, "UK", 2020),

            Artist(i++, "Julie Andrews", 1935, "UK", null),
            Artist(i++, "Christopher Plummer", 1929, "Canada", 2021),

            Artist(i++, "Eddie Murphy", 1961, "New York, NY", null),
            Artist(i++, "Arsenio Hall", 1956, "Cleveland, OH", null),
            Artist(i++, "James Earl Jones", 1931, "Arkabutla, MS", null),
            Artist(i++, "John Amos", 1939, "Newark, NJ", null),

            Artist(i++, "Leonardo DiCaprio", 1974, "Los Angeles, CA", null),
            Artist(i++, "Kate Winslet", 1975, "UK", null),
            Artist(i++, "Billy Zane", 1966, "Chicago, IL", null),

            )

        database.artistDao().insertAll(*list)
    }

}