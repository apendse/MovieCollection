package com.aap.ro.movies.ui

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.ui.test.TestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieListFragmentTests {


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var rule: ActivityScenarioRule<TestActivity> = ActivityScenarioRule(TestActivity::class.java)

    @Inject lateinit var movieRepository: MovieRepository

    lateinit var fragment: MovieListFragment
    @Before
    fun setup() {
        hiltRule.inject()
        fragment = MovieListFragment()
        val scenario = rule.scenario
        Log.d("YYYY", "fragment_loadingView_isDisplayed 1")
        scenario.onActivity {
            val transaction = it.supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment_container_view, fragment)
            transaction.commit()
            Log.d("YYYY", "fragment_loadingView_isDisplayed after commit")
        }
    }

    @After
    fun tearDown() {
        val scenario = rule.scenario

        scenario.onActivity {
            val fragments = it.supportFragmentManager.fragments
            fragments.forEach { fragment ->
                Log.d("YYYY", "Teardown $fragment")
                val transaction = it.supportFragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            }
        }
    }

     @Test
    fun fragment_loadingView_isDisplayed() {
         (movieRepository as? TestMovieRepository)?.let  {
            it.delay = 10000L
        }
        val  scenario = rule.scenario
         scenario.moveToState(Lifecycle.State.RESUMED)

         onView(withId(R.id.spinner_container)).check(matches(isDisplayed()))
         Log.d("YYYY", "fragment_loadingView_isDisplayed after onView")


     }

    @Test
    fun fragment_movieList_isDisplayed() {
        (movieRepository as? TestMovieRepository)?.let  {
            it.delay = 0L
        }
        val scenario = rule.scenario
        scenario.moveToState(Lifecycle.State.RESUMED)
        val list = listOf(MovieVO(123, "name", 2023, genre = emptyList(), directors = emptyList(), actors = emptyList()))
        Log.d("YYYY", "fragment_movieList_isDisplayed before populate")
        runBlocking {
            fragment.movieListViewModel.privateLoadingStateFlow.emit(false)
        }
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            fragment.populateList(list)
        }

        onView(withId(R.id.movie_list)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_container)).check(matches(not(isDisplayed())))

    }


}