package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.feature.*
import tk.zielony.carbonsamples.widget.SVGActivity

@ActivityAnnotation(title = R.string.featuresActivity_title, layout = R.layout.activity_samplelist)
class FeaturesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds tons of useful features to all popular widgets. These include rounded corners, HTML text, elevation system, anchors and others",
                SampleActivityItem(GestureDetectorActivity::class.java, R.drawable.ic_gesture_black_24dp),
                SampleActivityItem(AutoSizeTextActivity::class.java),
                SampleActivityItem(BehaviorActivity::class.java),
                SampleActivityItem(HtmlActivity::class.java),
                SampleActivityItem(XmlFontActivity::class.java, R.drawable.ic_font_download_black_24dp),
                SampleActivityItem(ShadowActivity::class.java),
                SampleActivityItem(PaginationActivity::class.java),
                SampleActivityItem(TextAppearanceActivity::class.java),
                SampleActivityItem(SVGActivity::class.java),
                SampleActivityItem(ZOrderActivity::class.java),
                SampleActivityItem(RoundedCornersActivity::class.java, R.drawable.ic_rounded_corner_black_24dp),
                SampleActivityItem(AnchorActivity::class.java),
                SampleActivityItem(TextMarkerActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
