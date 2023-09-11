package com.aap.ro.movies.repository

import com.aap.ro.movies.data.Genre
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
import org.junit.Assert
import org.junit.Test

class MovieRepositoryImplTest {
    @Test
    fun testGetGenreAsList() {
        val testGenre = action +
                thriller +
                drama +
                war +
                fantasy +
                romance +
                adventure +
                sciFi +
                family +
                crime +
                comedy +
                western

        val list = MovieRepositoryImpl.getGenreAsList(testGenre)
        val genres = Genre.values()

        genres.forEach {
            Assert.assertTrue(list.contains(it))
        }
    }

    @Test
    fun testGenreToInt() {
        val genres = Genre.values()

        var mask = 0
        genres.forEach {
            val current = MovieRepositoryImpl.getCombinedGenreAsInt(listOf(it))

            Assert.assertNotEquals(0, current)
            Assert.assertEquals(0, mask and current)

            mask += current
        }

        genres.forEach {
            val current = MovieRepositoryImpl.getCombinedGenreAsInt(listOf(it))
            Assert.assertTrue(mask and current != 0)
        }
    }
}