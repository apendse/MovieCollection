package com.aap.ro.movies.viewmodel

import com.aap.ro.movies.data.ArtistVO
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.room.ArtistWithRole
import com.aap.ro.movies.room.Movie
import com.aap.ro.movies.room.action
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
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
    fun verify_deleteIsCalled_correctly() {
        every { movieRepository.deleteMovie(any()) } just runs

        val id = 1234
        viewModel.deleteMovie(id)

        verify { movieRepository.deleteMovie(id) }
    }

    @Test
    fun verify_getMovieDetail_updatesState() = runTest {
        val id = 5678
        val movieDetail = Movie(id, "Test name", 2001, action)
        val artist1 = ArtistWithRole(1, "artist1", 1977, "Fremont, CA", null, MovieRepository.ACTOR)
        val artist2 =
            ArtistWithRole(2, "artist2", 1987, "Los Angeles, CA", null, MovieRepository.ACTOR)
        every { movieRepository.getMovieDetail(id) }.returns(flow { emit(movieDetail) })
        every { movieRepository.getMovieArtists(id) }.returns(flow {
            emit(
                listOf(
                    artist1,
                    artist2,
                )
            )
        })
        val actualMovie = viewModel.uiState.first()
        val artistVO1 = ArtistVO(1, "artist1", 1977, "Fremont, CA", null)
        val artistVO2 = ArtistVO(2, "artist2", 1987, "Los Angeles, CA", null)
        val expectedMovie = MovieVO(
            id, "Test name", 2001, listOf(Genre.ACTION),
            directors = emptyList(), actors = listOf(artistVO1, artistVO2)
        )

        viewModel.getMovieDetail(id)

        Assert.assertEquals(expectedMovie, actualMovie)

    }
}