package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.demo.*

@SampleAnnotation(titleId = R.string.demosActivity_title, layoutId = R.layout.activity_samplelist)
class DemosActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular problem solutions and other fun ideas implemented with Carbon's widgets and features",
                SampleActivityItem(PowerMenuActivity::class.java),
                SampleActivityItem(AutoCompleteActivity::class.java, true),
                SampleActivityItem(QuickReturnActivity::class.java),
                SampleActivityItem(SearchToolbarActivity::class.java),
                SampleActivityItem(ShareToolbarActivity::class.java, true),
                SampleActivityItem(ProfileActivity::class.java),
                SampleActivityItem(MusicPlayerActivity::class.java, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
