package carbon.component

import android.view.ViewGroup
import carbon.R
import carbon.beta.BottomSheetLayout
import carbon.widget.ImageView
import carbon.widget.Label

class BottomSheetRow(parent: ViewGroup?) : LayoutComponent<BottomSheetLayout.Item>(parent, R.layout.carbon_bottomsheet_row) {
    private val carbonItemIcon: ImageView = view.findViewById(R.id.carbon_itemIcon)
    private val carbonItemText: Label = view.findViewById(R.id.carbon_itemText)

    override fun bind(data: BottomSheetLayout.Item) {
        super.bind(data)
        carbonItemIcon.setImageDrawable(data.icon)
        if (data.iconTintList != null)
            carbonItemIcon.setTintList(data.iconTintList)
        carbonItemText.text = data.title
    }
}