package com.aap.ro.movies.viewmodel

import com.aap.ro.movies.data.ArtistVO
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.repository.MovieRepositoryImpl.Companion.ACTOR
import com.aap.ro.movies.room.ArtistWithRole
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.action
import com.aap.ro.movies.room.drama
import com.aap.ro.movies.room.western
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieDetailViewModelTest {

    private lateinit var viewModel: MovieDetailViewModel

    @MockK
    lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MovieDetailViewModel(movieRepository)
    }

    @Test
    fun verify_deleteIsCalled_correctly() = runTest {
        coEvery { movieRepository.deleteMovie(any()) } just runs

        val id = 1234
        viewModel.deleteMovie(id)

        coVerify { movieRepository.deleteMovie(id) }
    }

    @Test
    fun verify_getMovieDetail_updatesState() = runTest {
        val id = 5678
        val movieDetail = Movie(id, "Test name", 2001, (action or drama or western))
        val artist1 = ArtistWithRole(1, "artist1", 1977, "Fremont, CA", null, ACTOR)
        val artist2 =
            ArtistWithRole(2, "artist2", 1987, "Los Angeles, CA", null, ACTOR)
        // make the mock movie repository emit the test objects
        every { movieRepository.getMovieDetail(id) }.returns(flow {
            emit(movieDetail)
        })
        every { movieRepository.getMovieArtists(id) }.returns(flow {
            emit(
                listOf(
                    artist1,
                    artist2,
                )
            )
        })

        val artistVO1 = ArtistVO(1, "artist1", 1977, "Fremont, CA", null)
        val artistVO2 = ArtistVO(2, "artist2", 1987, "Los Angeles, CA", null)
        val expectedMovie = MovieVO(
            id, "Test name", 2001, listOf(Genre.ACTION, Genre.DRAMA, Genre.WESTERN,),
            directors = emptyList(), actors = listOf(artistVO1, artistVO2)
        )

        // This causes the ui state to be emitted with the movie detail
        viewModel.getMovieDetail(id)
        val actualMovie = viewModel.uiState.first()

        // verify that the actual movie is what we expected
        Assert.assertEquals(expectedMovie, actualMovie)

    }
}