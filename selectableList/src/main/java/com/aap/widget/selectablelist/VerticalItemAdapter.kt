package com.aap.widget.selectablelist

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aap.widget.selectablelist.databinding.ViewVerticalListRowBinding

class VerticalItemAdapter(private val selectableData: SelectableData) :
    Adapter<VerticalListViewHolder>() {

    private val selectableDataList = selectableData.getItemList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalListViewHolder {
        val binding =
            ViewVerticalListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val child = selectableDataList[0].createView(binding.container)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        binding.container.addView(child, layoutParams)
        return VerticalListViewHolder(binding, this::checkListener)
    }

    private fun checkListener(position: Int, isChecked: Boolean) {
        selectableDataList[position].setSelected(isChecked)
    }

    override fun getItemCount() = selectableDataList.size

    override fun onBindViewHolder(holder: VerticalListViewHolder, position: Int) {
        val data = selectableDataList[position]
        data.bindData(holder.viewVerticalListRowBinding.container.getChildAt(0))
        holder.viewVerticalListRowBinding.selected.isChecked = data.isSelected()
    }
}

class VerticalListViewHolder(
    val viewVerticalListRowBinding: ViewVerticalListRowBinding,
    val listener: (Int, Boolean) -> Unit
) : ViewHolder(viewVerticalListRowBinding.root) {
    init {
        viewVerticalListRowBinding.selected.setOnCheckedChangeListener { _, checked ->
            listener(adapterPosition, checked)
        }
    }
}