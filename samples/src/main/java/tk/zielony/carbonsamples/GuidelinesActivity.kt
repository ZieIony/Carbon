package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.guidelines.*

@ActivityAnnotation(
        title = R.string.guidelinesActivity_title,
        layout = R.layout.activity_samplelist,
        icon = R.drawable.ic_android_black_24dp
)
class GuidelinesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Sample screens taken from Material Design guidelines",
                SampleActivityItem(ButtonsUsageActivity::class.java),
                SampleActivityItem(MenusBehaviorActivity::class.java),
                SampleActivityItem(ShrineActivity::class.java, true),
                SampleActivityItem(CraneActivity::class.java),
                SampleActivityItem(BasilActivity::class.java, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}

