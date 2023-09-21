package com.aap.ro.movies.ui.movielist

import android.app.Activity
import android.graphics.drawable.LevelListDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.FragmentMovieListBinding
import com.aap.ro.movies.imageloader.Imageloader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier.PRIVATE
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieClickListener {

    private var _binding: FragmentMovieListBinding? = null

    lateinit var searchView: SearchView

    @Inject
    lateinit var imageLoader: Imageloader

    @VisibleForTesting
    val movieListViewModel: MovieListViewModel by viewModels()

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var movieGridAdapter: MovieGridAdapter



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("YYYY", "***** onCreateView")
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        return binding.root

    }

    @OptIn(FlowPreview::class)
    private fun createMenu() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.movie_list_action_menu, menu)
                val searchMenuItem = menu.findItem(R.id.action_search)
                searchMenuItem?.setOnActionExpandListener(object: OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        // no op
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        movieListViewModel.queryStringFlow.value = ""
                        return true
                    }

                })
                searchView = searchMenuItem.actionView as? SearchView  ?: return
                searchView.onActionViewCollapsed()
                lifecycle.coroutineScope.launch {
                    searchView.setSearchViewEventHandlers()
                    movieListViewModel.queryStringFlow.debounce(300).flatMapLatest { query ->
                        movieListViewModel.obtainMovieList(query)
                    }.collect {
                        populateList(it)
                    }
                }
//
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d("YYYY", "onMenuItemSelected id:${menuItem.itemId} title:${menuItem.title}")
                return when (menuItem.itemId) {
                    R.id.action_add -> {
                        showAddMovieUi()
                        true
                    }
                    R.id.action_search -> {
                        handleSearchClick(searchView = menuItem.actionView as? SearchView)
                        true
                    }

                    else -> false
                }
            }

            override fun onMenuClosed(menu: Menu) {
                Log.d("YYYY", "onMenu closed")
                super.onMenuClosed(menu)
            }
        }, viewLifecycleOwner)
    }

    private fun handleSearchClick(searchView: SearchView?) {
        searchView?.let {
            it.isIconified = false
        }
    }
    private fun showAddMovieUi() {
        findNavController().navigate(R.id.addMovieFragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        createMenu()
        lifecycle.coroutineScope.launch {
            movieListViewModel.loadDataIfNeeded()
            launch {
                movieListViewModel.loadingState.collect {
                    showOrHideSpinner(it)
                }
            }


        }
    }

    private fun SearchView.setSearchViewEventHandlers()  {

        setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {

                q?.let {
                    movieListViewModel.queryStringFlow.value = it
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                Log.d("YYYY", "onQueryTextChange text $newText")
                return false
            }

        })
        setOnCloseListener {
            Log.d("YYYY", "onClose called")
            movieListViewModel.queryStringFlow.value = ""
            false
        }

    }

    @VisibleForTesting
    fun showOrHideSpinner(isLoading: Boolean) {
        binding.spinnerContainer.visibility = if (isLoading) { VISIBLE } else { GONE }
    }
    @VisibleForTesting(otherwise = PRIVATE)
    fun populateList(list: List<MovieVO>) {
        dismissSoftKeyboard(searchView)
        movieListAdapter.submitList(list)
        movieGridAdapter.submitList(list)
    }

    private fun dismissSoftKeyboard(view: View) {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }


    private fun initList() {
        binding.movieList.layoutManager = LinearLayoutManager(requireContext())
        movieListAdapter = MovieListAdapter(this, imageLoader)
        movieGridAdapter = MovieGridAdapter(this, imageLoader)
        binding.movieList.adapter = movieListAdapter
        setModeImage(true)
        binding.viewModeSwitcher.setOnCheckedChangeListener {_, isChecked ->
            setModeImage(isChecked)
            setAdapter(isChecked)
        }
    }

    private fun setModeImage(isChecked: Boolean) {
        val levelListDrawable = binding.modeIcon.drawable as? LevelListDrawable ?: return
        levelListDrawable.level = if (isChecked) { 0 } else { 1 }
    }

    private fun setAdapter(isChecked: Boolean) {
        if (isChecked) {
            binding.movieList.layoutManager = LinearLayoutManager(requireContext())
            binding.movieList.adapter = movieListAdapter

        } else {
            binding.movieList.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.movieList.adapter = movieGridAdapter
        }
        // Animate the adapter
        binding.movieList.viewTreeObserver.addOnPreDrawListener(object:
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.movieList.viewTreeObserver.removeOnPreDrawListener(this)
                val childCount = binding.movieList.childCount
                for (i in 0 until childCount) {
                    val child = binding.movieList.getChildAt(i)
                    child.alpha = 0.0f
                    child.translationX = 0f
                    child.animate().alpha(1.0f).translationX(1f).setDuration(1000L).setStartDelay(i*50L).start()
                }
                return true
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickMovie(image: View, id: Int) {
        val key = resources.getString(R.string.movieId)
        val sharedElement = resources.getString(R.string.movie_image)
        val bundle = bundleOf(key to id)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            Pair(image, sharedElement))
        val extras = ActivityNavigatorExtras(options)
        findNavController().navigate(R.id.movieDetailFragment, bundle, null, extras)
    }
}