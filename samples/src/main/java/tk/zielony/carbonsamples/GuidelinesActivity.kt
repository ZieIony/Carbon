package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.guidelines.*

@SampleAnnotation(
        titleId = R.string.guidelinesActivity_title,
        layoutId = R.layout.activity_samplelist,
        iconId = R.drawable.ic_android_black_24dp
)
class GuidelinesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "Sample screens taken from Material Design guidelines",
                SampleActivityItem(ButtonsUsageActivity::class.java),
                SampleActivityItem(MenusBehaviorActivity::class.java),
                SampleActivityItem(ShrineActivity::class.java, true),
                SampleActivityItem(CraneActivity::class.java),
                SampleActivityItem(BasilActivity::class.java, true)
        ))
    }

}

