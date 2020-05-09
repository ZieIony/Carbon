package tk.zielony.carbonsamples.demo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DefaultHeaderItem
import carbon.component.DefaultImageTextSubtextDateItem
import carbon.component.ImageTextSubtextDateRow
import carbon.component.PaddedHeaderRow
import carbon.recycler.RowListAdapter
import kotlinx.android.synthetic.main.activity_collapsingtoolbar.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.Target
import tk.zielony.randomdata.common.DateGenerator
import tk.zielony.randomdata.common.DrawableImageGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import tk.zielony.randomdata.transformer.DateToStringTransformer
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_collapsingtoolbar, titleId = R.string.collapsingToolbarActivity_title)
class CollapsingToolbarActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val items = listOf(
                DefaultHeaderItem("Header"),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultHeaderItem("Header"),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultHeaderItem("Header"),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultHeaderItem("Header"),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem(),
                DefaultImageTextSubtextDateItem())

        val randomData = RandomData()
        randomData.addGenerator(Drawable::class.java, DrawableImageGenerator(this))
        randomData.addGenerator(String::class.java, StringNameGenerator().withMatcher { f: Target -> f.name == "text" && f.declaringClass == DefaultImageTextSubtextDateItem::class.java })
        randomData.addGenerator(String::class.java, TextGenerator().withMatcher { f: Target -> f.name == "subtext" })
        randomData.addGenerator(String::class.java, DateGenerator().withTransformer(DateToStringTransformer()))
        randomData.fill(items)

        recycler.layoutManager = LinearLayoutManager(this)
        val adapter:RowListAdapter<Serializable> = RowListAdapter<Serializable>()
        adapter.putFactory(DefaultImageTextSubtextDateItem::class.java, { parent: ViewGroup -> ImageTextSubtextDateRow(parent) })
        adapter.putFactory(DefaultHeaderItem::class.java, { parent: ViewGroup -> PaddedHeaderRow(parent) })
        recycler.adapter = adapter
        adapter.items = items
    }
}
