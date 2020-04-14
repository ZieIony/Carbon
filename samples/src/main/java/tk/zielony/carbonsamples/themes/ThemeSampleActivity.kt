package tk.zielony.carbonsamples.themes

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DataBindingComponent
import carbon.component.DefaultAvatarTextRatingSubtextDateItem
import carbon.component.DefaultHeaderItem
import carbon.component.PaddedHeaderRow
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import carbon.widget.TabLayout
import kotlinx.android.synthetic.main.activity_theme2.*
import tk.zielony.carbonsamples.ColorsActivity
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.SamplesActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.DateGenerator
import tk.zielony.randomdata.common.IntegerGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import tk.zielony.randomdata.transformer.DateToStringTransformer
import java.io.Serializable

open class ThemeSampleActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        tabs.setItems(arrayOf(
                TabLayout.Item("Recent"),
                TabLayout.Item("Favourites"),
                TabLayout.Item("Other")
        ))

        val items = listOf(
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
                DefaultAvatarTextRatingSubtextDateItem())

        val randomData = RandomData().apply {
            addGenerator(Drawable::class.java, DrawableAvatarGenerator(this@ThemeSampleActivity))
            addGenerator(String::class.java, StringNameGenerator().withMatcher { f -> f.name == "text" && f.declaringClass == DefaultAvatarTextRatingSubtextDateItem::class.java })
            addGenerator(Int::class.java, IntegerGenerator(0, 5).withMatcher { f -> f.name == "rating" })
            addGenerator(String::class.java, TextGenerator(4, false).withMatcher { f -> f.name == "subtext" })
            addGenerator(String::class.java, DateGenerator().withTransformer(DateToStringTransformer()))
        }
        randomData.fill(items)

        recycler.layoutManager = LinearLayoutManager(this)


        val adapter: RowListAdapter<Serializable> = RowListAdapter(DefaultAvatarTextRatingSubtextDateItem::class.java, RowFactory {
            DataBindingComponent<DefaultAvatarTextRatingSubtextDateItem>(it, R.layout.row_windowsnews)
        })

        adapter.putFactory(DefaultHeaderItem::class.java, { PaddedHeaderRow(it) })


        recycler.adapter = adapter
        adapter.items = items
    }
}

@SampleAnnotation(titleId = R.string.currentThemeActivity_title, layoutId = R.layout.activity_theme2)
class CurrentThemeActivity : ThemeSampleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    private fun applyTheme() {
        val preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE)
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 2)].value)
        theme.applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true)
        theme.applyStyle(ColorsActivity.secondary[preferences.getInt(ColorsActivity.SECONDARY, 14)].value, true)
    }
}

@SampleAnnotation(titleId = R.string.windowsThemeActivity_title, layoutId = R.layout.activity_theme2)
class WindowsThemeActivity : ThemeSampleActivity()
