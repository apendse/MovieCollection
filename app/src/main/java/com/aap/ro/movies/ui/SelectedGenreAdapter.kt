package com.aap.ro.movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.databinding.SelectableChipBinding

class GenreAdapter(genresParam: List<Genre>): RecyclerView.Adapter<GenreViewHolder>() {
    private val genres = mutableListOf<Genre>()
    init {
        genres.addAll(genresParam)
    }
    override fun getItemCount() = genres.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = SelectableChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)

    }
    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val chip = genres[position]
        holder.binding.genreChip.chip.text = chip.name
    }

}

class GenreViewHolder(val binding: SelectableChipBinding): RecyclerView.ViewHolder(binding.root)

