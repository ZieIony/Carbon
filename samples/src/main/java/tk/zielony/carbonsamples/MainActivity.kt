package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem

class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this, getString(R.string.app_name), false)

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityGroup(NewSamplesActivity::class.java),
                SampleActivityGroup(FavouritesActivity::class.java),
                SampleActivityGroup(AnimationsActivity::class.java),
                SampleActivityItem(ColorsActivity::class.java),
                SampleActivityGroup(ComponentsActivity::class.java),
                SampleActivityGroup(DemosActivity::class.java),
                SampleActivityGroup(DialogsActivity::class.java),
                SampleActivityGroup(FeaturesActivity::class.java),
                SampleActivityGroup(GuidelinesActivity::class.java),
                SampleActivityGroup(LibrariesActivity::class.java),
                SampleActivityGroup(WidgetsActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
