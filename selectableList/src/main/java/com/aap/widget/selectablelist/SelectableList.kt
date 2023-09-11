package com.aap.widget.selectablelist

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aap.widget.selectablelist.databinding.ViewSelectableBinding
import com.aap.widget.selectablelist.databinding.ViewVerticalListBinding

// class MyContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,  defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr), View.OnDragListener {
class SelectableList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), DialogInterface.OnClickListener,
    View.OnClickListener {
    // Horizontal list with customizable views.
    //clicking on the list will show a dropdown that will show
    private val binding: ViewSelectableBinding
    private var popupDialog: AlertDialog? = null
    var selectableData: SelectableData = emptyData()

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewSelectableBinding.inflate(inflater, this, true)
        binding.selectedItems.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)

    }

    private fun emptyData(): SelectableData {
        return object : SelectableData {
            override fun getItemList() = emptyList<SelectableDataItem>()

            override fun getEmptyListString() = ""

            override fun selectionListTitle() = ""
        }
    }

    fun setData(selectableData: SelectableData) {
        this.selectableData = selectableData
        binding.selectedItems.adapter = HorizontalListItemAdapter(selectableData, this)
    }


    override fun onClick(v: View?) {
        showPopupDialog()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        //dialog?.dismiss()
    }

    private fun showPopupDialog() {
        val builder = AlertDialog.Builder(context)
        with(builder) {
            setTitle(R.string.drop_down_title)

            val v = getVerticalList()

            setView(v)
            create().apply {
                setCanceledOnTouchOutside(true)
                setOnDismissListener {
                    updateListInHorizontalContainer()
                }
            }.show()
        }
    }

    private fun updateListInHorizontalContainer() {
        binding.selectedItems.adapter = HorizontalListItemAdapter(selectableData, this)
    }

    private fun getVerticalList(): View {
        val binding = ViewVerticalListBinding.inflate(LayoutInflater.from(context))
        binding.verticalItemList.layoutManager = LinearLayoutManager(context)
        binding.verticalItemList.adapter = VerticalItemAdapter(selectableData)
        return binding.root
    }
}