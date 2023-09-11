package com.aap.ro.movies.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.FragmentMovieDetailBinding
import com.aap.ro.movies.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class MovieDetailFragment : Fragment(), HeaderTitleProvider {

    private var _binding: FragmentMovieDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()
    private lateinit var artistAdapter: ArtistAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun createMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.movie_detail_action_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_delete -> {
                        deleteMovieDlg()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        createMenu()
        val key = resources.getString(R.string.movieId)

        val movieId = arguments?.getInt(key) ?: -1
        initUi()
        lifecycleScope.launch {
            movieDetailViewModel.uiState.collect {
                populateMovieDetails(it)
            }
        }
        lifecycleScope.launch {
            movieDetailViewModel.getMovieDetail(movieId)
        }

    }

    private fun initUi() {
        artistAdapter = ArtistAdapter(this)
        binding.artisList.layoutManager = LinearLayoutManager(requireContext())
        binding.artisList.adapter = artistAdapter
        binding.deleteMovie.setOnClickListener {
            deleteMovieDlg()
        }
    }


    private fun deleteMovieDlg() {
        val confirmationDialog = AlertDialog.Builder(requireContext()).setMessage(getString(R.string.delete_dialog, movieDetailViewModel.getMovieName()))
                        .setPositiveButton(android.R.string.ok) { dialog: DialogInterface , _ ->
                            deleteMovie()
                            dialog.dismiss()
                        }
                        .setNegativeButton(android.R.string.cancel){dialog: DialogInterface , _ ->
                            dialog.dismiss()
                        }.create()
        confirmationDialog.show()
    }

    private fun deleteMovie() {
        lifecycleScope.launch {
            val key = resources.getString(R.string.movieId)
            val movieId = arguments?.getInt(key) ?: -1
            movieDetailViewModel.deleteMovie(movieId)
            findNavController().navigateUp()
        }
    }

    @VisibleForTesting
    fun populateMovieDetails(movie: MovieVO) {
        binding.progressIndicator.visibility = View.GONE
        if (movie.yearOfRelease > 0) {
            binding.movieTitleWithYear.text = resources.getString(R.string.movie_title_with_year, movie.name, movie.yearOfRelease)
            binding.genreContainer.addGenreList(movie.genre)
            artistAdapter.setData(movie.directors, movie.actors)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getDirectorText(): String {
        return getString(R.string.director)
    }

    override fun getActorText(): String {
        return getString(R.string.actor)
    }
}