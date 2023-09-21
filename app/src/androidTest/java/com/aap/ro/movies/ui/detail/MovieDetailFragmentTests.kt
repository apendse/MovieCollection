package com.aap.ro.movies.ui.detail

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aap.ro.movies.R
import com.aap.ro.movies.repository.MovieRepository
import com.aap.ro.movies.ui.test.TestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieDetailFragmentTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var rule: ActivityScenarioRule<TestActivity> = ActivityScenarioRule(TestActivity::class.java)

    @Inject
    lateinit var movieRepository: MovieRepository

    lateinit var fragment: MovieDetailFragment

    @Before
    fun setup() {
        hiltRule.inject()
        fragment = MovieDetailFragment()

        val scenario = rule.scenario
        scenario.onActivity {
            val transaction = it.supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment_container_view, fragment)
            transaction.commit()
        }
    }

    @After
    fun tearDown() {
        val scenario = rule.scenario

        scenario.onActivity {
            val fragments = it.supportFragmentManager.fragments
            fragments.forEach { fragment ->
                val transaction = it.supportFragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            }
        }
    }
}