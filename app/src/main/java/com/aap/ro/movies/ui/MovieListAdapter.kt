package com.aap.ro.movies.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.GenreChipBinding
import com.aap.ro.movies.databinding.MovieListRowBinding

val diffCallback = object: DiffUtil.ItemCallback<MovieVO>() {
    override fun areItemsTheSame(oldItem: MovieVO, newItem: MovieVO) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieVO, newItem: MovieVO) = oldItem == newItem

}
interface MovieClickListener {
    fun onClickMovie(id: Int)
}

class MovieListAdapter(private val movieClickListener: MovieClickListener): ListAdapter<MovieVO, MovieListEntryViewHolder>(diffCallback) {
    private val oddBackground = ColorDrawable(0xffffffff.toInt())
    private val evenBackGround = ColorDrawable(0xffffcccc.toInt())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListEntryViewHolder {
        val binding = MovieListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListEntryViewHolder(binding) {
            val movie = getItem(it)
            movieClickListener.onClickMovie(movie.id)
        }
    }

    override fun onBindViewHolder(holder: MovieListEntryViewHolder, position: Int) {
        val movie = getItem(position)
        holder.binding.root.background = if (position % 2 == 0) { evenBackGround } else { oddBackground }
        with(holder.binding) {
            movieTitle.text = movie.name
            movieYear.text = "${movie.yearOfRelease}"
            genreContainer.removeAllViews()
            movie.genre.forEach {
                genreContainer.addView(genreContainer.createChip(it))
            }
        }
    }

    private fun ViewGroup.createChip(genre: Genre): TextView {
        val binding = GenreChipBinding.inflate(LayoutInflater.from(context), this, false )
        binding.chip.text = genre.name
        return binding.root
    }
}

class MovieListEntryViewHolder(val binding: MovieListRowBinding, onClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener{
            onClick(adapterPosition)
        }
    }
}