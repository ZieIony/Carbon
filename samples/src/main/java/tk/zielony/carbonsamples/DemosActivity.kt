package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.demo.*

@ActivityAnnotation(title = R.string.demosActivity_title, layout = R.layout.activity_samplelist)
class DemosActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular problem solutions and other fun ideas implemented with Carbon's widgets and features",
                SampleActivityItem(PowerMenuActivity::class.java, R.drawable.ic_power_settings_new_black_24dp),
                SampleActivityItem(AutoCompleteActivity::class.java, 0, true),
                SampleActivityItem(QuickReturnActivity::class.java),
                SampleActivityItem(SearchToolbarActivity::class.java, R.drawable.carbon_search),
                SampleActivityItem(ShareToolbarActivity::class.java, R.drawable.ic_share_black_24dp, true),
                SampleActivityItem(ProfileActivity::class.java, R.drawable.ic_person_black_24dp),
                SampleActivityItem(MusicPlayerActivity::class.java, R.drawable.ic_play_arrow_black_24dp, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
