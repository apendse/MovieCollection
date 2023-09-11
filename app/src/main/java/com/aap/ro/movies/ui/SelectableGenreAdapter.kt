package com.aap.ro.movies.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aap.ro.movies.R
import com.aap.ro.movies.data.SelectableGenre
import com.aap.ro.movies.databinding.SelectableChipBinding

interface GenreItemClickListener {
    fun onClick(index: Int)
}

interface GenreClickListener {
    fun onGenreClicked(selectableGenre: SelectableGenre)
}

class SelectableGenreAdapter(context: Context, private val genreClickListener: GenreClickListener): RecyclerView.Adapter<SelectableGenreViewHolder>(), GenreItemClickListener {
    private val genreList = mutableListOf<SelectableGenre>()
    private val selectedOverlay = ColorDrawable(Color.TRANSPARENT)
    private val unselectedOverlay = AppCompatResources.getDrawable(context, R.drawable.cross)//(0xf0000000.toInt())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableGenreViewHolder {
        val binding = SelectableChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectableGenreViewHolder(binding, this)
    }
    fun setGenre(list: List<SelectableGenre>) {
        genreList.clear()
        genreList.addAll(list)
        notifyDataSetChanged()
    }
    override fun onClick(index: Int) {
        genreClickListener.onGenreClicked(genreList[index])
    }
    override fun getItemCount() = genreList.size

    override fun onBindViewHolder(holder: SelectableGenreViewHolder, position: Int) {
        val chip = genreList[position]
        holder.binding.genreChip.chip.text = chip.genre.name
        holder.binding.overlay.background = if (chip.selected) { selectedOverlay } else { unselectedOverlay }
    }
}


class SelectableGenreViewHolder(val binding: SelectableChipBinding, private val genreItemClickListener: GenreItemClickListener): ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            genreItemClickListener.onClick(bindingAdapterPosition)
        }
    }
}