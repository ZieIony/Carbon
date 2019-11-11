package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.feature.*
import tk.zielony.carbonsamples.widget.SVGActivity

@SampleAnnotation(titleId = R.string.featuresActivity_title, layoutId = R.layout.activity_samplelist)
class FeaturesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds tons of useful features to all popular widgets. These include rounded corners, HTML text, elevation system, anchors and others",
                SampleActivityItem(GestureDetectorActivity::class.java),
                SampleActivityItem(AutoSizeTextActivity::class.java),
                SampleActivityItem(BehaviorActivity::class.java),
                SampleActivityItem(HtmlActivity::class.java),
                SampleActivityItem(XmlFontActivity::class.java),
                SampleActivityItem(ShadowActivity::class.java),
                SampleActivityItem(PaginationActivity::class.java),
                SampleActivityItem(TextAppearanceActivity::class.java),
                SampleActivityItem(SVGActivity::class.java),
                SampleActivityItem(ZOrderActivity::class.java),
                SampleActivityItem(RoundedCornersActivity::class.java),
                SampleActivityItem(AnchorActivity::class.java),
                SampleActivityItem(TextMarkerActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
