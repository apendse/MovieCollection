package com.aap.ro.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aap.ro.movies.R
import com.aap.ro.movies.data.AddMovieData
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.data.SelectableGenre
import com.aap.ro.movies.databinding.FragmentAddMovieBinding
import com.aap.ro.movies.viewmodel.AddMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 * Use the [AddMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AddMovieFragment : Fragment(), GenreClickListener, ArtistSelectionListener {

    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var artistAdapter: SelectableArtistAdapter
    private lateinit var genreAdapter: SelectableGenreAdapter

    val viewModel by  viewModels<AddMovieViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onArtistSelection(artist: SelectableArtist) {
        viewModel.changeArtistSelection(artist)
    }


    private fun getAvailableYears(): Array<Int> {
        val startYear = Calendar.getInstance().get(Calendar.YEAR) + 2
        return Array<Int>(100) {
            startYear - it
        }
    }

    override fun onGenreClicked(selectableGenre: SelectableGenre) {
        viewModel.onGenreClicked(selectableGenre)
    }
    private fun initUi() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, getAvailableYears())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = adapter
        binding.yearSpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setYear(parent?.getItemAtPosition(position) as? Int ?: -1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // NOOP
            }

        }
        binding.genrePicker.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        genreAdapter = SelectableGenreAdapter(requireContext(), this)
        binding.genrePicker.adapter = genreAdapter
        binding.genrePicker.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        binding.actors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        artistAdapter = SelectableArtistAdapter(this)
        binding.actors.adapter = artistAdapter
        binding.addMovieTitle.addTextChangedListener(afterTextChanged = {
             binding.saveMovie.isEnabled = it.toString().isNotEmpty()
        })
        binding.saveMovie.setOnClickListener {
            viewModel.addMovie(binding.addMovieTitle.text.toString())
            findNavController().navigateUp()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val key = resources.getString(R.string.movieId)
        val movieId = arguments?.getInt(key) ?: -1
        initUi()

        lifecycleScope.launch {

            viewModel.createMovieData(movieId)

        }
        lifecycleScope.launch {
            viewModel.uiState.collect {
                populateData(it)
            }
        }

    }


    private fun populateData(addMovieData: AddMovieData) {
        artistAdapter.submitList(addMovieData.selectedArtistList.toMutableList())
        genreAdapter.setGenre(addMovieData.genres)
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMovieFragment()
    }
}