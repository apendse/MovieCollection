package com.aap.ro.movies.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.aap.ro.movies.data.Genre
import com.aap.ro.movies.databinding.GenreChipBinding


class GenreContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,  defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.HORIZONTAL

    }

    fun addGenreList(list : List<Genre>) {
        removeAllViews()
        list.forEach {
            addView(createChip(it))
        }
    }

    private fun ViewGroup.createChip(genre: Genre): TextView {
        val binding = GenreChipBinding.inflate(LayoutInflater.from(context), this, false )
        binding.chip.text = genre.name
        return binding.root
    }
}