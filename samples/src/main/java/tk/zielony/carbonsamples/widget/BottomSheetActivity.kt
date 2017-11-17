package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.beta.BottomSheetLayout
import carbon.widget.DropDown
import kotlinx.android.synthetic.main.activity_bottomsheet.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_bottomsheet, title = R.string.bottomSheetActivity_title)
class BottomSheetActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomSheet.style = BottomSheetLayout.Style.List
        bottomSheet.setTitle("Menu")
        bottomSheet.setMenu(R.menu.menu_navigation)

        val items = arrayOf("List", "Grid")
        (dropDown as DropDown<String>).setItems(items)
        dropDown.setOnSelectionChangedListener { item, _ ->
            bottomSheet.style = if (item == "List")
                BottomSheetLayout.Style.List
            else
                BottomSheetLayout.Style.Grid
        }
    }
}
