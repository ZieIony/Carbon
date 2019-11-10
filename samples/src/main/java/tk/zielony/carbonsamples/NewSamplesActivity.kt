package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.demo.MusicPlayerActivity
import tk.zielony.carbonsamples.demo.ProfileActivity
import tk.zielony.carbonsamples.demo.SearchToolbarActivity
import tk.zielony.carbonsamples.feature.GestureDetectorActivity
import tk.zielony.carbonsamples.feature.ShadowActivity
import tk.zielony.carbonsamples.feature.XmlFontActivity
import tk.zielony.carbonsamples.graph.BarChartActivity
import tk.zielony.carbonsamples.graph.LineChartActivity
import tk.zielony.carbonsamples.widget.*

@ActivityAnnotation(title = R.string.newSamplesActivity_title, layout = R.layout.activity_samplelist)
class NewSamplesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "These samples are new or have significant changes since the previous release",
                SampleActivityItem(ButtonsActivity::class.java),
                SampleActivityItem(FlowLayoutActivity::class.java),
                SampleActivityItem(ShadowActivity::class.java),
                SampleActivityItem(BottomNavigationViewActivity::class.java),
                SampleActivityItem(XmlFontActivity::class.java, R.drawable.ic_font_download_black_24dp),
                SampleActivityItem(BackdropActivity::class.java),
                SampleActivityItem(MenusActivity::class.java, R.drawable.ic_menu_black_24dp),
                SampleActivityItem(BarChartActivity::class.java, 0, true),
                SampleActivityItem(LineChartActivity::class.java, 0, true),
                SampleActivityItem(SearchToolbarActivity::class.java, R.drawable.carbon_search),
                SampleActivityItem(MusicPlayerActivity::class.java, R.drawable.ic_play_arrow_black_24dp, true),
                SampleActivityItem(ProfileActivity::class.java, R.drawable.ic_person_black_24dp),
                SampleActivityItem(CheckBoxRadioActivity::class.java, R.drawable.carbon_checkbox_checked),
                SampleActivityItem(GestureDetectorActivity::class.java, R.drawable.ic_gesture_black_24dp),
                SampleActivityItem(FABActivity::class.java, R.drawable.ic_add_circle_black_24dp),
                SampleActivityItem(BannerActivity::class.java),
                SampleActivityItem(NavigationViewActivity::class.java, 0, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}

