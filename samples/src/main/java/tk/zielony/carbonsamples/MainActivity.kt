package tk.zielony.carbonsamples

import android.content.Intent
import android.os.Bundle
import carbon.component.PaddingItem
import kotlinx.android.synthetic.main.activity_main.*

@ActivityAnnotation(layout = R.layout.activity_main)
class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        colors.setOnClickListener {
            startActivity(Intent(this, ColorsActivity::class.java))
        }

        setItems(arrayOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                SampleActivityGroup(NewSamplesActivity::class.java, R.drawable.ic_new_releases_black_24dp),
                SampleActivityGroup(FavouritesActivity::class.java, R.drawable.ic_star_black_24dp),
                SampleActivityGroup(AnimationsActivity::class.java),
                SampleActivityGroup(ComponentsActivity::class.java, R.drawable.ic_view_compact_black_24dp),
                SampleActivityGroup(DemosActivity::class.java),
                SampleActivityGroup(WidgetsActivity::class.java, R.drawable.ic_widgets_black_24dp),
                SampleActivityGroup(ChartsActivity::class.java, R.drawable.ic_show_chart_black_24dp),
                SampleActivityGroup(FeaturesActivity::class.java),
                SampleActivityGroup(GuidelinesActivity::class.java, R.drawable.ic_android_black_24dp),
                SampleActivityGroup(LibrariesActivity::class.java),
                SampleActivityGroup(ThemesActivity::class.java),
                SampleActivityGroup(DialogsActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
