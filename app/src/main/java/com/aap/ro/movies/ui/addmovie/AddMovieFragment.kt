package com.aap.ro.movies.ui.addmovie

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aap.ro.movies.R
import com.aap.ro.movies.data.AddMovieData
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.data.SelectableGenre
import com.aap.ro.movies.databinding.FragmentAddMovieBinding
import com.aap.ro.movies.databinding.GenreChipBinding
import com.aap.ro.movies.ui.ArtistSelectionListener
import com.aap.ro.movies.ui.GenreAdapter
import com.aap.ro.movies.ui.GenreClickListener
import com.aap.ro.movies.ui.SelectableArtistAdapter
import com.aap.widget.selectablelist.SelectableData
import com.aap.widget.selectablelist.SelectableDataItem
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
    private lateinit var genreAdapter: GenreAdapter

    val viewModel by viewModels<AddMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onArtistSelection(artist: SelectableArtist, isSelected: Boolean) {
        //Log.d("YYYY", "onArtistSelection ${artist.artistVO.name} $isSelected")
        viewModel.changeArtistSelection(artist, isSelected)
    }


    private fun getAvailableYears(): Array<Int> {
        val startYear = Calendar.getInstance().get(Calendar.YEAR) - 6
        return Array<Int>(100) {
            startYear - it
        }
    }

    override fun onGenreClicked(selectableGenre: SelectableGenre) {
        //viewModel.onGenreClicked(selectableGenre)
    }

    private fun loadGenresInSelectableList() {
        val selectedGenres = viewModel.genres
        val selectableGenresList = selectedGenres.map { SelectableDataItemGenre(it) }
        binding.genreList.setData(SelectableDataForGenre(selectableGenresList, resources.getString(R.string.click_to_add_genre),
                        getString(R.string.genre_list_title)))
    }

    private fun initUi() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            getAvailableYears()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = adapter
        binding.yearSpinner.onItemSelectedListener = object : OnItemSelectedListener {
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


        binding.actors.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


        loadGenresInSelectableList()
        artistAdapter = SelectableArtistAdapter(this)
        binding.actors.adapter = artistAdapter
        binding.addMovieTitle.addTextChangedListener(afterTextChanged = {
            binding.saveMovie.isEnabled = it.toString().isNotEmpty()
        })

        binding.addMovieTitle.setOnFocusChangeListener{view, hasFocus ->
            if (!hasFocus) {
                dismissSoftKeyboard(view)
            }
        }
        binding.saveMovie.setOnClickListener {
            viewModel.addMovie(binding.addMovieTitle.text.toString(), getSelectedGenre())
            findNavController().navigateUp()
        }

    }

    private fun dismissSoftKeyboard(view: View) {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun getSelectedGenre(): List<SelectableGenre> {
        val selectableGenreData = binding.genreList.selectableData as? SelectableDataForGenre ?: throw RuntimeException("Not valid selected item data")
        return  selectableGenreData.getItemList().map {
            val selectableGenreItem = it as? SelectableDataItemGenre ?: throw RuntimeException("Not valid item type")
            selectableGenreItem.selectableGenre.copy(selected = it.isSelected())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val key = resources.getString(R.string.movieId)
        // val movieId = arguments?.getInt(key) ?: -1
        initUi()

        lifecycleScope.launch {
            viewModel.createEmptyMovieData()
        }
        lifecycleScope.launch {
            viewModel.uiState.collect {
                Log.d("YYYY", "Collect triggered")
                populateData(it)
            }
        }

    }

    private fun populateData(addMovieData: AddMovieData) {
        artistAdapter.submitList(addMovieData.selectedArtistList.toMutableList())
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMovieFragment()
    }
}

class SelectableDataForGenre(private val itemList: List<SelectableDataItem>, private val emptyListString: String, private val listTitleString: String): SelectableData {
    override fun getItemList() = itemList

    override fun getEmptyListString() = emptyListString

    override fun selectionListTitle() = listTitleString
}
class SelectableDataItemGenre(val selectableGenre: SelectableGenre) : SelectableDataItem {
    private var isSelected = selectableGenre.selected
    override fun isSelected() = isSelected

    override fun createView(parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GenreChipBinding.inflate(layoutInflater, parent, false).root
    }

    override fun setSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    override fun bindData(view: View) {
        val binding = GenreChipBinding.bind(view)
        binding.chip.text = selectableGenre.genre.toString()
    }

}
