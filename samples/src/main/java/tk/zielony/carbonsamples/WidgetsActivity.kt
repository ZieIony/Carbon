package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.widget.*

@ActivityAnnotation(title = R.string.widgetsActivity_title, layout = R.layout.activity_samplelist)
class WidgetsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Widgets with sample styles, data and applications",
                SampleActivityItem(BannerActivity::class.java),
                SampleActivityItem(CheckBoxRadioActivity::class.java, R.drawable.carbon_checkbox_checked),
                SampleActivityItem(ButtonsActivity::class.java),
                SampleActivityItem(FABActivity::class.java, R.drawable.ic_add_circle_black_24dp),
                SampleActivityItem(CircularProgressActivity::class.java),
                SampleActivityItem(MenusActivity::class.java, R.drawable.ic_menu_black_24dp),
                SampleActivityItem(ProgressBarsActivity::class.java),
                SampleActivityItem(SnackbarActivity::class.java),
                SampleActivityItem(TextFieldsActivity::class.java, R.drawable.ic_text_fields_black_24dp),
                SampleActivityItem(TabsActivity::class.java),
                SampleActivityItem(RecyclerActivity::class.java, R.drawable.ic_view_stream_black_24dp),
                SampleActivityItem(ExpandableRecyclerActivity::class.java),
                SampleActivityItem(ExpansionPanelActivity::class.java),
                SampleActivityItem(DropDownActivity::class.java, R.drawable.carbon_dropdown),
                SampleActivityItem(NavigationViewActivity::class.java, 0, true),
                SampleActivityItem(SeekBarActivity::class.java),
                SampleActivityItem(FlowLayoutActivity::class.java),
                SampleActivityItem(TableLayoutActivity::class.java),
                SampleActivityItem(BottomNavigationViewActivity::class.java),
                SampleActivityItem(BottomSheetActivity::class.java, 0, true),
                SampleActivityItem(BackdropActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
