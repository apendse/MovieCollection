package com.aap.ro.movies.ui.addmovie

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.aap.ro.movies.R
import com.aap.ro.movies.data.SelectableArtist
import com.aap.ro.movies.databinding.SelectableArtistRowBinding

class AddArtistDialog : DialogFragment(0) {
    private var artistList: List<SelectableArtist>? = null
    fun setArtistList(list: List<SelectableArtist>) {
        artistList = list
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_add_artists)
            .create()
        initUi(dialog)
        return dialog
    }

    private fun initUi(dialog: AlertDialog) {
        val artistList = this.artistList ?: throw RuntimeException()
        val autoComplete = dialog.findViewById<AppCompatAutoCompleteTextView>(R.id.dialog_query)
        autoComplete!!.setAdapter(ArtistAdapter(artistList))
    }

    class ArtistAdapter(private val originalList: List<SelectableArtist>) : BaseAdapter(),
        Filterable {
        private val mLock = Any()
        var matchingArtists: List<SelectableArtist>? = null
        override fun getCount() = matchingArtists?.size ?: 0

        override fun getItem(position: Int) = matchingArtists?.get(position)

        override fun getItemId(position: Int) =
            matchingArtists?.get(position)?.artistVO?.id?.toLong() ?: -1L

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val artist = matchingArtists?.get(position) ?: throw RuntimeException()
            return if (convertView == null) {
                val binding = SelectableArtistRowBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
                bind(binding, artist)
                binding.root
            } else {
                val binding = SelectableArtistRowBinding.bind(convertView)
                bind(binding, artist)
                convertView
            }
        }

        private fun bind(binding: SelectableArtistRowBinding, artist: SelectableArtist) {
            binding.artistName.text = artist.artistVO.name
            binding.selected.isChecked = artist.selected
        }
        override fun getFilter() = MyFilter()


        inner class MyFilter() : Filter() {
            override fun performFiltering(prefix: CharSequence?): FilterResults {
                if (prefix == null || prefix.length == 0) {
                    synchronized(mLock) {
                        matchingArtists = originalList
                    }
                    return FilterResults().apply {
                        values = matchingArtists
                        count = matchingArtists?.size ?: 0
                    }
                } else {
                    val filtered = originalList.filter {
                        it.artistVO.name.contains(prefix.toString(), true)
                    }

                    return FilterResults().apply {
                        count = filtered.size
                        values = filtered
                    }
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                matchingArtists = results?.values as? List<SelectableArtist> ?: emptyList()

                if (results?.count ?: 0 > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

        }


    }


}