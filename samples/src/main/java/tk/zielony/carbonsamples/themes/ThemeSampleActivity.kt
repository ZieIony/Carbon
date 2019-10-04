package tk.zielony.carbonsamples.themes

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.*
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import carbon.widget.TabLayout
import kotlinx.android.synthetic.main.activity_theme2.*
import tk.zielony.carbonsamples.*

import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.IntegerGenerator
import tk.zielony.randomdata.common.StringDateGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import java.io.Serializable

open class ThemeSampleActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this)

        tabs.setItems(arrayOf(
                TabLayout.Item("Recent"),
                TabLayout.Item("Favourites"),
                TabLayout.Item("Other")
        ))

        val items = listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                DefaultHeaderItem("Today"),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultHeaderItem("Yesterday"),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultHeaderItem("Monday"),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultHeaderItem("Tuesday"),
                DefaultAvatarTextRatingSubtextDateItem(),
                DefaultAvatarTextRatingSubtextDateItem(),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)))

        val randomData = RandomData()
        randomData.addGenerators(arrayOf(
                DrawableAvatarGenerator(this),
                StringNameGenerator().withMatcher { f -> f.name == "text" && f.declaringClass == DefaultAvatarTextRatingSubtextDateItem::class.java },
                IntegerGenerator(0, 5).withMatcher { f -> f.name == "rating" },
                TextGenerator(4, false).withMatcher { f -> f.name == "subtext" },
                StringDateGenerator()
        ))
        randomData.fill(items)

        recycler.layoutManager = LinearLayoutManager(this)


        val adapter: RowListAdapter<Serializable> = RowListAdapter(DefaultAvatarTextRatingSubtextDateItem::class.java, RowFactory {
            DataBindingComponent<DefaultAvatarTextRatingSubtextDateItem>(it, R.layout.row_windowsnews)
        })

        adapter.addFactory(PaddingItem::class.java, { PaddingRow(it) })
        adapter.addFactory(DefaultHeaderItem::class.java, { PaddedHeaderRow(it) })


        recycler.adapter = adapter
        adapter.items = items
    }
}

@ActivityAnnotation(title = R.string.currentThemeActivity_title, layout = R.layout.activity_theme2)
class CurrentThemeActivity : ThemeSampleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    private fun applyTheme() {
        val preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE)
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 2)].value)
        theme.applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true)
        theme.applyStyle(ColorsActivity.accents[preferences.getInt(ColorsActivity.ACCENT, 14)].value, true)
    }
}

@ActivityAnnotation(title = R.string.windowsThemeActivity_title, layout = R.layout.activity_theme2)
class WindowsThemeActivity : ThemeSampleActivity()
