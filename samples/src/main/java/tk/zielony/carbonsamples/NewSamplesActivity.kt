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

@ActivityAnnotation(
        title = R.string.newSamplesActivity_title,
        layout = R.layout.activity_samplelist,
        icon = R.drawable.ic_new_releases_black_24dp
)
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
                SampleActivityItem(XmlFontActivity::class.java),
                SampleActivityItem(BackdropActivity::class.java),
                SampleActivityItem(MenusActivity::class.java),
                SampleActivityItem(BarChartActivity::class.java, true),
                SampleActivityItem(LineChartActivity::class.java, true),
                SampleActivityItem(SearchToolbarActivity::class.java),
                SampleActivityItem(MusicPlayerActivity::class.java, true),
                SampleActivityItem(ProfileActivity::class.java),
                SampleActivityItem(CheckBoxRadioActivity::class.java),
                SampleActivityItem(GestureDetectorActivity::class.java),
                SampleActivityItem(FABActivity::class.java),
                SampleActivityItem(BannerActivity::class.java),
                SampleActivityItem(NavigationViewActivity::class.java, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}

