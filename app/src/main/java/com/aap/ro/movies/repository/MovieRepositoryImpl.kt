package com.aap.ro.movies.repository

import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.room.Artist
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.MovieDatabase
import com.aap.ro.movies.room.MovieToArtist
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
import javax.inject.Inject

/**
 * This is the real movie repository implementation. This will be injected using Hilt. Therefore it's
 * annotated with \@Inject constructor annotation.
 */

class MovieRepositoryImpl @Inject constructor(private val database: MovieDatabase): MovieRepository  {

    //@Inject
    //lateinit var  database: MovieDatabase
    override fun getMovieList() = database.movieDao().getMovieList()

    override fun getMovieCount() = database.movieDao().getMovieCount()

    override fun getAllArtists() = database.artistDao().getAllArtists()
    override fun getMovieDetail(id: Int) = database.movieDao().getMovieDetails(id)
    override fun getMovieArtists(movieId: Int) = database.movieToArtistDao().getArtistsForMovie(movieId)

    override fun addMovie(movievo: MovieVO): Long = database.movieDao().insert(convertToMovie(movievo))
    override fun addMovieArtists(movieArtists: Array<MovieToArtist>) = database.movieToArtistDao().insertAll(movieToArtist = *movieArtists)

    override fun deleteMovie(id: Int) {
        val movie = database.movieDao().getMovieDetailsWithoutFlow(id)
        database.movieDao().deleteMovie(movie)
    }

    private fun convertToMovie(movievo: MovieVO) : Movie {
        return Movie(-1, movievo.name, movievo.yearOfRelease, getGenre(movievo.genre))
    }

    private fun getGenre(list: List<Genre>): Int {
        var mask = 0
        genreList.forEach {
            if (list.contains(it.second)) {
                mask = mask or it.first
            }
        }
        return mask
    }

    override fun getMovieDatabasePopulator(): MovieDatabasePopulator {
        return MovieDatabasePopulatorImpl(database)
    }


    companion object {

        const val DIRECTOR = "director"
        const val ACTOR = "actor"
        private val genreList = listOf(
            action to Genre.ACTION,
            thriller to Genre.THRILLER,
            drama to Genre.DRAMA,
            war to Genre.WAR,
            fantasy to Genre.FANTASY,
            romance to Genre.ROMANCE,
            adventure to Genre.ADVENTURE,
            sciFi to Genre.SCI_FI,
            family to Genre.FAMILY,
            crime to Genre.CRIME,
            comedy to Genre.COMEDY,
        )

        fun getGenreAsList(g: Int): List<Genre> {
            val ret = arrayListOf<Genre>()
            genreList.forEach {
                if (it.first and g != 0) {
                    ret.add(it.second)
                }
            }
            return ret
        }
    }

    class MovieDatabasePopulatorImpl(private val database: MovieDatabase): MovieDatabasePopulator {
        override fun insertSampleMovieToArtist() {
            var i = 1
            val list = arrayOf(
                MovieToArtist(i++, 1, 1, ACTOR),
                MovieToArtist(i++, 1, 2, ACTOR),
                MovieToArtist(i++, 1, 3, ACTOR),

                MovieToArtist(i++, 2, 4, DIRECTOR),
                MovieToArtist(i++, 2, 5, ACTOR),
                MovieToArtist(i++, 2, 6, ACTOR),
                MovieToArtist(i++, 2, 7, ACTOR),

                MovieToArtist(i++, 3, 8, DIRECTOR),
                MovieToArtist(i++, 3, 9, ACTOR),
                MovieToArtist(i++, 3, 10, ACTOR),
                MovieToArtist(i++, 3, 11, ACTOR),
            )
            database.movieToArtistDao().insertAll(*list)
        }

        override fun insertSampleMovies() {

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

        override fun insertSampleArtists() {
            var i = 1
            val list = arrayOf(
                Artist(i++, "Bruce Willis", 1955, "West Germany", null),
                Artist(i++, "Alan Rickman", 1946, "UK", 2016),
                Artist(i++, "Bonnie Bedelia", 1948, "New York, NY", null),
                Artist(i++, "Steven Spielberg", 1946, "Cincinnati, OH", null), //4
                Artist(i++, "Tom Hanks", 1956, "Concord, CA", null),
                Artist(i++, "Matt Damon", 1970, "Cambridge, MA", null),
                Artist(i++, "Tom Sizemore", 1961, "Detroit, MI", 2023),
                Artist(i++, "Jerry Zucker", 1965, "Homestead, FL", null), //8
                Artist(i++, "Patric Swayze", 1952, "Houston, TX", 2009), //9
                Artist(i++, "Demi Moore", 1962, "Roswell, NM", null),  // 10
                Artist(i++, "Whoopi Goldberg", 1955, "New York, NY", null), //11
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

                Artist(i++, "Sylvester Stallone", 1946, "New York, NY", null),
                Artist(i++, "Brian Dennehy", 1938, "Bridgeport, CT", 2020),
                Artist(i++, "Keanu Reeves", 1964, "Lebanon", null),
                Artist(i++, "Sandra Bullock", 1964, "Washington, DC", null),
                Artist(i++, "Dennis Hopper", 1936, "Dodge City, KS", 2010),
            )

            database.artistDao().insertAll(*list)
        }
    }

}