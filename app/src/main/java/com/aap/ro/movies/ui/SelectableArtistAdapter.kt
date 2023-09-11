package com.aap.ro.movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.databinding.SelectableArtistRowBinding

val diff = object : DiffUtil.ItemCallback<SelectableArtist>() {
    override fun areItemsTheSame(oldItem: SelectableArtist, newItem: SelectableArtist) = oldItem.artistVO.id == newItem.artistVO.id

    override fun areContentsTheSame(oldItem: SelectableArtist, newItem: SelectableArtist): Boolean {
        return oldItem.selected == newItem.selected && oldItem.artistVO == newItem.artistVO
    }



}

interface ArtistSelectionListener {
    fun onArtistSelection(artist: SelectableArtist, isSelected: Boolean)
}
interface SelectionChangedListener {
    fun onSelectionChanged(index: Int, checked: Boolean)
}
class SelectableArtistAdapter(private val artistSelectListener: ArtistSelectionListener): ListAdapter<SelectableArtist, SelectableArtistViewHolder>(diff), SelectionChangedListener {

    override fun onSelectionChanged(index: Int, checked: Boolean) {
        artistSelectListener.onArtistSelection(getItem(index), isSelected = checked)
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
        binding.selected.setOnCheckedChangeListener {_, isChecked ->
            selectionChangedListener.onSelectionChanged(bindingAdapterPosition, isChecked)
        }
    }
}

