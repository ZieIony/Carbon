package tk.zielony.carbonsamples

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import carbon.recycler.ViewItemDecoration
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
                "New and starred",
                SampleActivityGroup(NewSamplesActivity::class.java),
                SampleActivityGroup(FavouritesActivity::class.java),
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
                SampleActivityGroup(DialogsActivity::class.java)
        )

        val preferences = getSharedPreferences("samples", Context.MODE_PRIVATE)
        val recentlyUsed = preferences.getString(RECENTLY_USED, null)
        recentlyUsed?.let {
            items.addAll(0, listOf(
                    "The most recently used sample",
                    SampleActivityGroup(Class.forName(it) as Class<out Activity>)
            ))
        }

        val decoration = ViewItemDecoration(this, R.layout.carbon_row_divider)
        decoration.setDrawBefore { position -> position > 0 && items[position] is String }
        recycler.addItemDecoration(decoration)

        setItems(items)
    }

}
