package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.beta.BottomSheetLayout
import carbon.widget.DropDown
import kotlinx.android.synthetic.main.activity_bottomsheet.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@ActivityAnnotation(layout = R.layout.activity_bottomsheet, title = R.string.bottomSheetActivity_title)
class BottomSheetActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        bottomSheet.style = BottomSheetLayout.Style.List
        bottomSheet.setTitle("Menu")
        bottomSheet.setMenu(R.menu.menu_navigation)

        val items = arrayOf("List", "Grid")
        (dropDown as DropDown).setItems(items)
        dropDown.setOnSelectionChangedListener { item: String, _ ->
            bottomSheet.style = if (item == "List")
                BottomSheetLayout.Style.List
            else
                BottomSheetLayout.Style.Grid
        }
    }
}
