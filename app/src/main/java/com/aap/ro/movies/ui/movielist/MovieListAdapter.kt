package com.aap.ro.movies.ui.movielist

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aap.ro.movies.data.MovieVO
import com.aap.ro.movies.databinding.MovieListRowBinding
import com.aap.ro.movies.imageloader.Imageloader

val diffCallback = object: DiffUtil.ItemCallback<MovieVO>() {
    override fun areItemsTheSame(oldItem: MovieVO, newItem: MovieVO) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieVO, newItem: MovieVO) = oldItem == newItem

}
interface MovieClickListener {
    fun onClickMovie(image: View, id: Int)
}
//val oddBackground = ColorDrawable(0xffffffff.toInt())
val oddBackground = ColorDrawable(0xffcccccc.toInt())
val evenBackGround = ColorDrawable(0xffccaaaa.toInt())

class MovieListAdapter(private val movieClickListener: MovieClickListener, private val imageLoader: Imageloader): ListAdapter<MovieVO, MovieListEntryViewHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListEntryViewHolder {
        val binding = MovieListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListEntryViewHolder(binding) {
            val movie = getItem(it)
            movieClickListener.onClickMovie(binding.imageView, movie.id)
        }
    }

    override fun onBindViewHolder(holder: MovieListEntryViewHolder, position: Int) {
        val movie = getItem(position)
        holder.binding.root.background = if (position % 2 == 0) { evenBackGround } else { oddBackground }
        with(holder.binding) {
            movieTitle.text = movie.name
            movieYear.text = "${movie.yearOfRelease}"
            genreContainer.addGenreList(movie.genre)
            movie.thumbNail?.let { url ->
                imageLoader.loadImageInto(imageView, url)
            }
        }
    }

}

class MovieListEntryViewHolder(val binding: MovieListRowBinding, onClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener{
            onClick(bindingAdapterPosition)
        }
    }
}