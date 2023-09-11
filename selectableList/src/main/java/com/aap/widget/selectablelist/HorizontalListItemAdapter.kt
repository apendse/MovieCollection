package com.aap.widget.selectablelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aap.widget.selectablelist.databinding.ViewPlaceholderBinding



class HorizontalListItemAdapter(private val selectableData: SelectableData, private val clickListener: View.OnClickListener): RecyclerView.Adapter<ItemViewHolder>() {

    private val selectedDataList = getItems(selectableData.getItemList())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = selectedDataList[0].createView(parent)
        v.setOnClickListener {
            clickListener.onClick(it)
        }
        return ItemViewHolder(v)
    }

    private fun getItems(selectableDataList: List<SelectableDataItem>): List<SelectableDataItem> {
        val items = selectableDataList.filter { it.isSelected() }
        return if (items.isEmpty()) {
            listOf(placeHolderItem(selectableData))
        } else {
            items
        }
    }

    private fun placeHolderItem(selectableData: SelectableData): SelectableDataItem {
        return object: SelectableDataItem {
            override fun isSelected(): Boolean {
                return false
            }

            override fun createView(parent: ViewGroup): View {
                val view = ViewPlaceholderBinding.inflate(LayoutInflater.from(parent.context)).root
                val textView = view as? TextView
                textView?.let {
                    it.text = selectableData.getEmptyListString()
                }
                return view
            }

            override fun setSelected(isSelected: Boolean) {
                // noop
            }

            override fun bindData(view: View) {
                //noop
            }

        }
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        selectedDataList[position].bindData(holder.itemView)
    }


    override fun getItemCount() = selectedDataList.size
}

class ItemViewHolder(view: View): RecyclerView.ViewHolder(view)

