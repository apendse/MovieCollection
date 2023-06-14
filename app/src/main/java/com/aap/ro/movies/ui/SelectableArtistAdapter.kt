package com.aap.ro.movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.databinding.SelectableArtistRowBinding

val diff = object : DiffUtil.ItemCallback<SelectableArtist>() {
    override fun areItemsTheSame(oldItem: SelectableArtist, newItem: SelectableArtist) = oldItem == newItem

    override fun areContentsTheSame(oldItem: SelectableArtist, newItem: SelectableArtist) = areItemsTheSame(oldItem, newItem)



}

interface ArtistSelectionListener {
    fun onArtistSelection(artist: SelectableArtist)
}
interface SelectionChangedListener {
    fun onSelectionChanged(index: Int,)
}
class SelectableArtistAdapter(private val artistSelectListener: ArtistSelectionListener): ListAdapter<SelectableArtist, SelectableArtistViewHolder>(diff), SelectionChangedListener {

    override fun onSelectionChanged(index: Int) {
        artistSelectListener.onArtistSelection(getItem(index))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableArtistViewHolder {
        val binding = SelectableArtistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectableArtistViewHolder(binding, this)
    }


    override fun onBindViewHolder(holder: SelectableArtistViewHolder, position: Int) {
        val selectableArtist = getItem(position)
        with(holder.binding) {
            artistName.text = selectableArtist.artistVO.name
            selected.isChecked = selectableArtist.selected
        }

    }

}

class SelectableArtistViewHolder(val binding: SelectableArtistRowBinding, private val selectionChangedListener: SelectionChangedListener): ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {_ ->
            selectionChangedListener.onSelectionChanged(adapterPosition)
        }
    }
}

