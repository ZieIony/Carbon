package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.widget.*

@SampleAnnotation(
        titleId = R.string.widgetsActivity_title,
        layoutId = R.layout.activity_samplelist,
        iconId = R.drawable.ic_widgets_black_24dp
)
class WidgetsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "Widgets with sample styles, data and applications",
                SampleActivityItem(BannerActivity::class.java),
                SampleActivityItem(CheckBoxRadioActivity::class.java),
                SampleActivityItem(ButtonsActivity::class.java),
                SampleActivityItem(FABActivity::class.java),
                SampleActivityItem(CircularProgressActivity::class.java),
                SampleActivityItem(MenusActivity::class.java),
                SampleActivityItem(ProgressBarsActivity::class.java),
                SampleActivityItem(SnackbarActivity::class.java),
                SampleActivityItem(TextFieldsActivity::class.java),
                SampleActivityItem(TabsActivity::class.java),
                SampleActivityItem(RecyclerActivity::class.java),
                SampleActivityItem(ExpandableRecyclerActivity::class.java),
                SampleActivityItem(ExpansionPanelActivity::class.java),
                SampleActivityItem(DropDownActivity::class.java),
                SampleActivityItem(NavigationViewActivity::class.java, true),
                SampleActivityItem(SeekBarActivity::class.java),
                SampleActivityItem(FlowLayoutActivity::class.java),
                SampleActivityItem(TableLayoutActivity::class.java),
                SampleActivityItem(BottomNavigationViewActivity::class.java),
                SampleActivityItem(BottomSheetActivity::class.java, true),
                SampleActivityItem(BackdropActivity::class.java)
        ))
    }

}
