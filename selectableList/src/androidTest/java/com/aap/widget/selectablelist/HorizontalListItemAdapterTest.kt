package com.aap.widget.selectablelist

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HorizontalListItemAdapterTest {

    private lateinit var horizontalListItemAdapter: HorizontalListItemAdapter
    private var appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private var onClickCalled = false
    @Before
    fun setup() {
        horizontalListItemAdapter = HorizontalListItemAdapter(FakeSelectableData()) {
            onClickCalled = true
        }
    }
    @Test
    fun horizontalListItemAdapter_createdViewHolder_handlesOnClickCorrectly() {
        onClickCalled = false
        val viewGroup = FrameLayout(appContext)
        val vh = horizontalListItemAdapter.createViewHolder(viewGroup, 0)

        vh.itemView.performClick()

        Assert.assertTrue(onClickCalled)
    }

    @Test
    fun horizontalListItemAdapter_afterCreation_returnsCorrectSize() {
        val fakeSelectableDataForTest = FakeSelectableData()
        val list = listOf(FakeSelectableDataItem(), FakeSelectableDataItem(), FakeSelectableDataItem()).apply {
            this[0].isSelectedFlag = false
            this[1].isSelectedFlag = true
            this[2].isSelectedFlag = true
        }
        fakeSelectableDataForTest.list = list

        horizontalListItemAdapter = HorizontalListItemAdapter(fakeSelectableDataForTest, {})

        Assert.assertEquals(2, horizontalListItemAdapter.itemCount)
    }


    @Test
    fun horizontalListItemAdapter_verify_bind() {

    }

}

class FakeSelectableDataItem: SelectableDataItem {
    var isSelectedFlag = false
    override fun isSelected() = isSelectedFlag

    override fun createView(parent: ViewGroup): View {
        return TextView(parent.context)
    }

    override fun setSelected(isSelected: Boolean) {
        this.isSelectedFlag = isSelected
    }

    override fun bindData(view: View) {
        //no op
    }

}
class FakeSelectableData: SelectableData {
    var emptyString = "test"
    var selectionListTitleString = "click here to select items"
    var list : List<SelectableDataItem> = listOf(FakeSelectableDataItem(), FakeSelectableDataItem())
    override fun getItemList(): List<SelectableDataItem> = list

    override fun getEmptyListString() = emptyString

    override fun selectionListTitle() =
        selectionListTitleString


}