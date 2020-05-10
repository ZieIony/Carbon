package carbon.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import carbon.R
import carbon.recycler.RowFactory


open class ToolStrip : MenuStrip {
    constructor(context: Context) : super(context, null, R.attr.carbon_toolStripStyle)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, R.attr.carbon_toolStripStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        itemFactory = RowFactory { parent: ViewGroup -> ToolItemComponent(parent) }
        checkableItemFactory = RowFactory { parent: ViewGroup -> CheckableToolItemComponent(parent) }
    }
}