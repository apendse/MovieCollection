package com.aap.widget.selectablelist

import android.view.View
import android.view.ViewGroup

interface SelectableData {
    fun getItemList(): List<SelectableDataItem>
    fun getEmptyListString(): String

    fun selectionListTitle(): String
}
interface SelectableDataItem {
    fun isSelected(): Boolean
    fun createView(parent: ViewGroup): View

    fun setSelected(isSelected: Boolean)
    fun bindData(view: View)

}