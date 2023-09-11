package com.aap.ro.movies.ui

import android.app.Activity
import android.content.Context
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
import android.view.inputmethod.InputMethodManager
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aap.ro.movies.R
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.FragmentMovieListBinding
import com.aap.ro.movies.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier.PRIVATE

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieClickListener {

    private var _binding: FragmentMovieListBinding? = null

    lateinit var searchView: SearchView

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
    ): View {
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
                        Log.d("YYYY", "Menu item $item expanded")
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        Log.d("YYYY", "Menu item $item collapsed")
                        // no op
                        queryString.value = ""
                        return true
                    }

                })
                searchView = searchMenuItem.actionView as? SearchView  ?: return
                searchView.onActionViewCollapsed()
                lifecycle.coroutineScope.launch {
                    searchView.getQueryTextFlow().debounce(300).distinctUntilChanged().flatMapLatest { query ->
                        Log.d("YYYY", "****** query $query")
                        movieListViewModel.obtainMovieList(query)
                    }.collect {
                        Log.d("YYYY", "Movie list   ${it.size}")
                        populateList(it)
                    }
                }
//                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        Log.d("YYYY", "Query : $query")
//                        query?.let {
//                            movieListViewModel.query = query
//                        }
//                        return true
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        Log.d("YYYY", "text $newText")
//                        return false
//                    }
//
//                })
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


//            Uses flow and repeatOnLifeCycle
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                movieListViewModel.obtainMovieList("").collect {
//                    populateList(it)
//
//                }
//            }
        }
    }
    val queryString = MutableStateFlow("")
    private fun SearchView.getQueryTextFlow(): StateFlow<String> {

        setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {

                q?.let {
                    Log.d("YYYY", "on submit Query : $queryString")
                    queryString.value = it
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
            queryString.value = ""
            false
        }
        return queryString
    }

    @VisibleForTesting
    fun showOrHideSpinner(isLoading: Boolean) {
        binding.spinnerContainer.visibility = if (isLoading) { VISIBLE } else { GONE }
    }
    @VisibleForTesting(otherwise = PRIVATE)
    fun populateList(list: List<MovieVO>) {
        dismissSoftKeyboard(searchView)
        movieListAdapter.submitList(list)
    }

    private fun dismissSoftKeyboard(view: View) {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

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
        findNavController().navigate(R.id.movieDetailFragment, bundle)
    }
}