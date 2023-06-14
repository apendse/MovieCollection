package com.aap.ro.movies.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovieDatabaseTests {
    private lateinit var movieDao: MovieDao
    private lateinit var artistDao: ArtistDao
    private lateinit var movieToArtistDao: MovieToArtistDao
    private lateinit var db: MovieDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Note : This is an in-memory database. It does not persist
        db = Room.inMemoryDatabaseBuilder(
            context, MovieDatabase::class.java).build()
        movieDao = db.movieDao()
        artistDao = db.artistDao()
        movieToArtistDao = db.movieToArtistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun verify_movieTable_movieInsertedIsCorrectlyRead() = runBlocking {
        val id = 1
        val movieName = "Test Movie"
        val year = 1999
        val genre = 12
        val movie = Movie(id, movieName, year, genre)

        movieDao.insert(movie)
        Log.d("YYYY", "Inserted")

        val movieFromDb = movieDao.getMovieDetails(id).first()

        Assert.assertEquals(id, movieFromDb.id)
        Assert.assertEquals(genre, movieFromDb.genre)
        Assert.assertEquals(movieName, movieFromDb.movieName)


    }

    @Test
    fun verify_artistTable_writtenRowIsQueriedById() {
        val artist = Artist(1, "John Wayne",1907, "Winterset, IA", 1979)
        artistDao.insertAll(artist)

        val artistFromDB = artistDao.getArtist(1)
        Assert.assertEquals(artist, artistFromDB)
    }

    @Test
    fun verify_movieToArtistTable_returnsArtistsForMovie() = runBlocking {
        val movieId = 1
        val artist1 = Artist(1, "John Wayne",1907, "Winterset, IA", 1979)
        val artist2 = Artist(2, "Jimmy Stewart",1908, "Indiana, PA", 1997)
        val artist3 = Artist(3, "John Ford",1894, "Cape Elizabeth, MD", 1973)
        val movie = Movie(movieId, "The Man Who Shot Liberty Valance", 1962, drama)
        artistDao.insertAll(artist1, artist2, artist3)
        movieDao.insert(movie)
        val movieToArtist1 = MovieToArtist(1, movieId, 1, ROLE_ACTOR)
        val movieToArtist2 = MovieToArtist(2, movieId, 2, ROLE_ACTOR)
        val movieToArtist3 = MovieToArtist(3, movieId, 3, ROLE_DIRECTOR)

        movieToArtistDao.insertAll(movieToArtist1, movieToArtist2, movieToArtist3)

        val artists = movieToArtistDao.getArtistsForMovie(movieId).first()
        Assert.assertEquals(3, artists.size)
        val firstActorFromList = artists.find { artistWithRole -> artistWithRole.id == 1 }
        Assert.assertEquals(artist1.createArtistWithRole(ROLE_ACTOR), firstActorFromList)

        val directorFromList = artists.find { artistWithRole -> artistWithRole.id == 3} ?: throw RuntimeException("Did not find the director")
        Assert.assertEquals(artist3.createArtistWithRole(ROLE_DIRECTOR), directorFromList)
    }

    private fun Artist.createArtistWithRole(role: String) = ArtistWithRole(id, name, yearOfBirth, placeOfBirth, yearOfDeath, role)

}