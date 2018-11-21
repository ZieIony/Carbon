package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem

class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this, getString(R.string.app_name), false)

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityGroup(NewSamplesActivity::class.java, getString(R.string.newSamplesActivity_title)),
                SampleActivityGroup(FavouritesActivity::class.java, getString(R.string.favouritesActivity_title)),
                SampleActivityGroup(AnimationsActivity::class.java, getString(R.string.animationsActivity_title)),
                SampleActivityItem(ColorsActivity::class.java, getString(R.string.colorsActivity_title)),
                SampleActivityGroup(ComponentsActivity::class.java, getString(R.string.componentsActivity_title)),
                SampleActivityGroup(DemosActivity::class.java, getString(R.string.demosActivity_title)),
                SampleActivityGroup(DialogsActivity::class.java, getString(R.string.dialogsActivity_title)),
                SampleActivityGroup(FeaturesActivity::class.java, getString(R.string.featuresActivity_title)),
                SampleActivityGroup(GuidelinesActivity::class.java, getString(R.string.guidelinesActivity_title)),
                SampleActivityGroup(LibrariesActivity::class.java, getString(R.string.librariesActivity_title)),
                SampleActivityGroup(WidgetsActivity::class.java, getString(R.string.widgetsActivity_title)),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
