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
import com.aap.ro.movies.room.western
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

/**
 * This is the real movie repository implementation. This will be injected using Hilt. Therefore it's
 * annotated with \@Inject constructor annotation.
 */

class MovieRepositoryImpl @Inject constructor(private val database: MovieDatabase) :
    MovieRepository {

    //@Inject
    //lateinit var  database: MovieDatabase
    override fun getMovieList(query: String): Flow<List<Movie>> {
        val flow1 = if (query.isEmpty()) database.movieDao().getMovieList() else emptyFlow()
        val flow2 =
            if (query.isEmpty()) emptyFlow() else database.movieDao().getMatchingMovies(query)
        return merge(flow1, flow2)
    }

    override fun getMovieCount() = database.movieDao().getMovieCount()

    override fun getAllArtists() = database.artistDao().getAllArtists()
    override fun getMovieDetail(id: Int) = database.movieDao().getMovieDetails(id)
    override fun getMovieArtists(movieId: Int) =
        database.movieToArtistDao().getArtistsForMovie(movieId)

    override fun addMovie(movievo: MovieVO): Long =
        database.movieDao().insert(convertToMovie(movievo))

    override fun addMovieArtists(movieArtists: Array<MovieToArtist>) =
        database.movieToArtistDao().insertAll(movieToArtist = movieArtists)

    override suspend fun deleteMovie(id: Int) {
        database.movieDao().getMovieDetails(id).collect { movie ->
            database.movieDao().deleteMovie(movie)
        }

    }

    private fun convertToMovie(movievo: MovieVO): Movie {
        return Movie(
            movievo.id,
            movievo.name,
            movievo.yearOfRelease,
            getCombinedGenreAsInt(movievo.genre),
            null,
            null
        )
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
            western to Genre.WESTERN
        )

        fun getCombinedGenreAsInt(list: List<Genre>): Int {
            var mask = 0
            genreList.forEach {
                if (list.contains(it.second)) {
                    mask = mask or it.first
                }
            }
            return mask
        }

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

    class MovieDatabasePopulatorImpl(private val database: MovieDatabase) : MovieDatabasePopulator {
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

                MovieToArtist(i++, 10, 28, ACTOR),
                MovieToArtist(i++, 10, 29, ACTOR),
                MovieToArtist(i++, 10, 30, ACTOR),
                MovieToArtist(i++, 10, 31, DIRECTOR),

                )
            database.movieToArtistDao().insertAll(*list)
        }

        override fun insertSampleMovies() {
            var i = 1
            val list = arrayOf(
                Movie(
                    i++,
                    "Die Hard",
                    1989,
                    action + thriller,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/yFihWxQcmqcaBR31QM6Y8gT6aYV.jpg",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/yFihWxQcmqcaBR31QM6Y8gT6aYV.jpg"
                ),
                Movie(
                    i++,
                    "Saving Private Ryan ",
                    1998,
                    drama + war,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/uqx37cS8cpHg8U35f9U5IBlrCV3.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Ghost",
                    1999,
                    drama + fantasy + romance,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/w9RaPHov8oM5cnzeE27isnFMsvS.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Indiana Jones and the Last Crusade",
                    1989,
                    action + adventure + fantasy,
                    null,
                    null
                ),
                Movie(
                    i++,
                    "Jurassic Park",
                    1993,
                    action + adventure + sciFi,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg"
                ),
                Movie(
                    i++,
                    "Goldfinger",
                    1964,
                    action + adventure + thriller,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/6fTzum7gSpLWww26WvWjETNqfD9.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Sound of Music",
                    1965,
                    drama + family,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/pDuoh2fKuacDXYtpREJysMOzQmS.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Pulp Fiction",
                    1994,
                    drama + crime,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Coming to America",
                    1988,
                    comedy + romance,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/djRAvxyvvN2yqlJKDbT3uy4vOBw.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Titanic",
                    1997,
                    drama + romance,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Jaws",
                    1975,
                    adventure + thriller,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/lxM6kqilAdpdhqUl2biYp5frUxE.jpg",
                    null
                ),
                Movie(
                    i++,
                    "First Blood",
                    1982,
                    action + adventure + thriller,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/bGIDYYOX7Cj1o7W8JiwHd3TzJVw.jpg",
                    null
                ),
                Movie(
                    i++,
                    "Ghostbusters",
                    1984,
                    action + comedy + fantasy,
                    "https://www.themoviedb.org/t/p/w94_and_h141_bestv2/3EYgeouoO5hUHF9eYpQ2ADsJ9ba.jpg",
                    null
                ),
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
                Artist(i++, "James Cameron", 1954, "Ontario, Canada", null),

                Artist(i++, "Sylvester Stallone", 1946, "New York, NY", null),
                Artist(i++, "Brian Dennehy", 1938, "Bridgeport, CT", 2020),
                Artist(i++, "Ted Kotcheff", 1931, "Toronto, Canada", null),
                Artist(i++, "Keanu Reeves", 1964, "Lebanon", null),
                Artist(i++, "Sandra Bullock", 1964, "Washington, DC", null),
                Artist(i++, "Dennis Hopper", 1936, "Dodge City, KS", 2010),
            )

            database.artistDao().insertAll(*list)
        }
    }

}