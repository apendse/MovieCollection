package com.aap.ro.movies.ui

import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieDatabasePopulator
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryModule
import com.aap.ro.movies.room.Artist
import com.aap.ro.movies.room.ArtistWithRole
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.MovieToArtist
import com.aap.ro.movies.room.action
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MovieRepositoryModule::class]
)
abstract class FakeMovieRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMovieRepository(
        fakeMovieRepository: TestMovieRepository
    ): MovieRepository
}

class TestMovieRepository @Inject constructor(): MovieRepository {
    var delay: Long = 0L
    override fun getMovieList(): Flow<List<Movie>> {
        return flow<List<Movie>> {
            delay(delay)
            emit(listOf(Movie(1, "test", 1998, action)))
        }
    }

    override fun getMovieCount(): Int {
        runBlocking {
            delay(delay)
        }
        return 0
    }

    override fun getAllArtists(): Flow<List<Artist>>
        = flow<List<Artist>> {
            delay(delay)
            emit(emptyList())
        }


    override fun getMovieDetail(id: Int): Flow<Movie> = flow {
        delay(delay)
        emit(Movie(0, "", 0, 0))
    }

    override fun getMovieArtists(movieId: Int): Flow<List<ArtistWithRole>>  = flow<List<ArtistWithRole>> {
        delay(delay)
        emit(emptyList())
    }



    override fun addMovie(movievo: MovieVO): Long {
        return -1L
    }

    override fun addMovieArtists(movieArtists: Array<MovieToArtist>) {

    }

    override fun deleteMovie(id: Int) {

    }

    override fun getMovieDatabasePopulator() = FakeMovieDatabasePopulator()
}

class FakeMovieDatabasePopulator: MovieDatabasePopulator {
    override fun insertSampleMovies() {
        // NOOP
    }

    override fun insertSampleArtists() {
        // NO OP
    }

    override fun insertSampleMovieToArtist() {
        //NO OP
    }

}