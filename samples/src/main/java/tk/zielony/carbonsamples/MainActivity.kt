package tk.zielony.carbonsamples

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import carbon.component.DividerItem
import carbon.component.PaddingItem
import kotlinx.android.synthetic.main.activity_main.*

@SampleAnnotation(layoutId = R.layout.activity_main)
class MainActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        colors.setOnClickListener {
            startActivity(Intent(this, ColorsActivity::class.java))
        }

        val items = mutableListOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "New and starred",
                SampleActivityGroup(NewSamplesActivity::class.java),
                SampleActivityGroup(FavouritesActivity::class.java),
                DividerItem(),
                "All samples grouped by category",
                SampleActivityGroup(AnimationsActivity::class.java),
                SampleActivityGroup(ComponentsActivity::class.java),
                SampleActivityGroup(DemosActivity::class.java),
                SampleActivityGroup(WidgetsActivity::class.java),
                SampleActivityGroup(ChartsActivity::class.java),
                SampleActivityGroup(FeaturesActivity::class.java),
                SampleActivityGroup(GuidelinesActivity::class.java),
                SampleActivityGroup(LibrariesActivity::class.java),
                SampleActivityGroup(ThemesActivity::class.java),
                SampleActivityGroup(DialogsActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        )

        val preferences = getSharedPreferences("samples", Context.MODE_PRIVATE)
        val recentlyUsed = preferences.getString(RECENTLY_USED, null)
        recentlyUsed?.let {
            items.addAll(1, listOf(
                    "The most recently used sample",
                    SampleActivityGroup(Class.forName(it) as Class<out Activity>),
                    DividerItem()
            ))
        }

        setItems(items)
    }

}
