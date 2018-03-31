package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem

class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this, getString(R.string.app_name), false)

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityItem(AnimationsActivity::class.java, getString(R.string.animationsActivity_title)),
                SampleActivityItem(ColorsActivity::class.java, getString(R.string.colorsActivity_title)),
                SampleActivityItem(ComponentsActivity::class.java, getString(R.string.componentsActivity_title)),
                SampleActivityItem(DemosActivity::class.java, getString(R.string.demosActivity_title)),
                SampleActivityItem(DialogsActivity::class.java, getString(R.string.dialogsActivity_title)),
                SampleActivityItem(FeaturesActivity::class.java, getString(R.string.featuresActivity_title)),
                SampleActivityItem(GuidelinesActivity::class.java, getString(R.string.guidelinesActivity_title)),
                SampleActivityItem(LibrariesActivity::class.java, getString(R.string.librariesActivity_title)),
                SampleActivityItem(WidgetsActivity::class.java, getString(R.string.widgetsActivity_title)),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
