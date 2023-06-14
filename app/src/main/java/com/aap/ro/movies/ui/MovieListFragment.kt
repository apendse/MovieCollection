package com.aap.ro.movies.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.FragmentFirstBinding
import com.aap.ro.movies.repository.MovieDatabaseFactory
import com.aap.ro.movies.repository.MovieRepositoryFactory
import com.aap.ro.movies.viewmodel.MovieListViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieListFragment : Fragment(), MovieClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val movieListViewModel by viewModels<MovieListViewModel> {
        MovieListViewModel.Factory
    }

    lateinit var movieListAdapter: MovieListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        lifecycle.coroutineScope.launch {
            movieListViewModel.loadDataIfNeeded()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.obtainMovieList().collect {
                    populateList(it)

                }
            }
        }

    }

    private fun populateList(list: List<MovieVO>) {
        binding.spinnerContainer.visibility = View.GONE
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