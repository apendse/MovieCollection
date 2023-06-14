package com.aap.ro.movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aap.ro.movies.data.ArtistVO
import com.aap.ro.movies.databinding.ArtistHeaderBinding
import com.aap.ro.movies.databinding.SelectableArtistRowBinding

interface HeaderTitleProvider {
    fun getDirectorText(): String
    fun getActorText(): String
}

class ArtistAdapter(private val headerTitleProvider: HeaderTitleProvider): RecyclerView.Adapter<ViewHolder>() {

    val list = mutableListOf<Any>()
    private val artistType = 10
    private val headerType = 11

    fun setData(directors: List<ArtistVO>, actors: List<ArtistVO>) {
        list.clear()
        if (directors.isNotEmpty()) {
            list.add(headerTitleProvider.getDirectorText())
            directors.forEach {
                list.add(it)
            }
        }
        if (actors.isNotEmpty()) {
            list.add(headerTitleProvider.getActorText())
            actors.forEach {
                list.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            artistType -> {
                val binding = SelectableArtistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.selected.visibility = View.GONE
                ArtistViewHolder(binding)
            }

            headerType -> {
                val binding = ArtistHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }

            else -> throw RuntimeException("Unknown type")
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ArtistViewHolder) {
            holder.selectableArtistRowBinding.artistName.text = (list[position] as? ArtistVO)?.name ?: ""
        } else if (holder is HeaderViewHolder) {
            holder.artistHeaderBinding.headerTitle.text = list[position] as? String ?: ""
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) { is String -> headerType else -> artistType }
    }
}

class ArtistViewHolder(val selectableArtistRowBinding: SelectableArtistRowBinding): ViewHolder(selectableArtistRowBinding.root) {

}
class HeaderViewHolder(val artistHeaderBinding: ArtistHeaderBinding): ViewHolder(artistHeaderBinding.root) {

}