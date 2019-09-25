package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem

class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this, getString(R.string.app_name), false)

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityGroup(NewSamplesActivity::class.java, R.drawable.ic_new_releases_black_24dp),
                SampleActivityGroup(FavouritesActivity::class.java, R.drawable.ic_star_black_24dp),
                SampleActivityGroup(AnimationsActivity::class.java),
                SampleActivityItem(ColorsActivity::class.java, R.drawable.ic_color_lens_black_24dp),
                SampleActivityGroup(ComponentsActivity::class.java, R.drawable.ic_view_compact_black_24dp),
                SampleActivityGroup(DemosActivity::class.java),
                SampleActivityGroup(DialogsActivity::class.java),
                SampleActivityGroup(FeaturesActivity::class.java),
                SampleActivityGroup(GuidelinesActivity::class.java),
                SampleActivityGroup(LibrariesActivity::class.java),
                SampleActivityGroup(WidgetsActivity::class.java, R.drawable.ic_widgets_black_24dp),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
