package tk.zielony.carbonsamples

import android.content.Intent
import android.os.Bundle
import carbon.component.PaddingItem
import carbon.widget.ImageView

@ActivityAnnotation(layout = R.layout.activity_main)
class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<ImageView>(R.id.colors).setOnClickListener {
            startActivity(Intent(this, ColorsActivity::class.java))
        }

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityGroup(NewSamplesActivity::class.java, R.drawable.ic_new_releases_black_24dp),
                SampleActivityGroup(FavouritesActivity::class.java, R.drawable.ic_star_black_24dp),
                SampleActivityGroup(AnimationsActivity::class.java),
                SampleActivityGroup(ComponentsActivity::class.java, R.drawable.ic_view_compact_black_24dp),
                SampleActivityGroup(DemosActivity::class.java),
                SampleActivityGroup(DialogsActivity::class.java),
                SampleActivityGroup(FeaturesActivity::class.java),
                SampleActivityGroup(GuidelinesActivity::class.java),
                SampleActivityGroup(LibrariesActivity::class.java),
                SampleActivityGroup(WidgetsActivity::class.java, R.drawable.ic_widgets_black_24dp),
                SampleActivityGroup(ThemesActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
