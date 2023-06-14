package com.aap.ro.movies.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.FragmentMovieListBinding
import com.aap.ro.movies.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier.PRIVATE

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieClickListener {

    private var _binding: FragmentMovieListBinding? = null

    @VisibleForTesting
    val movieListViewModel: MovieListViewModel by viewModels()

    private lateinit var movieListAdapter: MovieListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("YYYY", "MovieListFragment.onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("YYYY", "MovieListFragment.onAttach")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("YYYY", "MovieListFragment.onCreateView")
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        binding.addMovie.setOnClickListener {
            findNavController().navigate(R.id.addMovieFragment)
        }
        lifecycle.coroutineScope.launch {
            movieListViewModel.loadDataIfNeeded()
            launch {
                movieListViewModel.loadingState.collect {
                    showOrHideSpinner(it)
                }
            }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.obtainMovieList().collect {
                    populateList(it)

                }
            }
        }
    }

    @VisibleForTesting
    fun showOrHideSpinner(isLoading: Boolean) {
        binding.spinnerContainer.visibility = if (isLoading) { VISIBLE } else { GONE }
    }
    @VisibleForTesting(otherwise = PRIVATE)
    fun populateList(list: List<MovieVO>) {
        movieListAdapter.submitList(list)
    }

    private fun initList() {
        binding.movieList.layoutManager = LinearLayoutManager(requireContext())
        movieListAdapter = MovieListAdapter(this)
        binding.movieList.adapter = movieListAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickMovie(id: Int) {
        val key = resources.getString(R.string.movieId)
        val bundle = bundleOf(key to id)
        findNavController().navigate(R.id.MovieDetailFragment, bundle)
    }
}