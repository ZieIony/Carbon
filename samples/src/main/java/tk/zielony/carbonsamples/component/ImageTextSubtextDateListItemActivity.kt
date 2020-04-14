package tk.zielony.carbonsamples.component

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DefaultHeaderItem
import carbon.component.DefaultImageTextSubtextDateItem
import carbon.component.ImageTextSubtextDateRow
import carbon.component.PaddedHeaderRow
import carbon.recycler.RowListAdapter
import carbon.recycler.ViewItemDecoration
import kotlinx.android.synthetic.main.activity_listcomponent.*
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

@SampleAnnotation(layoutId = R.layout.activity_listcomponent, titleId = R.string.imageTextSubtextDateListItemActivity_title)
class ImageTextSubtextDateListItemActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val randomData = RandomData().apply {
            addGenerator(Drawable::class.java, DrawableImageGenerator(this@ImageTextSubtextDateListItemActivity))
            addGenerator(String::class.java, StringNameGenerator().withMatcher { f: Target -> f.name == "text" && f.declaringClass == DefaultImageTextSubtextDateItem::class.java })
            addGenerator(String::class.java, TextGenerator().withMatcher { f: Target -> f.name == "subtext" })
            addGenerator(String::class.java, DateGenerator().withTransformer(DateToStringTransformer()).withMatcher { f: Target -> f.name == "date" })
        }

        val adapter = RowListAdapter<Serializable>().apply {
            putFactory(DefaultImageTextSubtextDateItem::class.java, { ImageTextSubtextDateRow(it) })
            putFactory(DefaultHeaderItem::class.java, { PaddedHeaderRow(it) })
        }

        adapter.items = listOf(
                DefaultHeaderItem("Header"),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                DefaultHeaderItem("Header"),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                DefaultHeaderItem("Header"),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                DefaultHeaderItem("Header"),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java),
                randomData.generate(DefaultImageTextSubtextDateItem::class.java))

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val paddingItemDecoration = ViewItemDecoration(this, R.layout.carbon_row_padding)
        paddingItemDecoration.setDrawBefore { it == 0 }
        paddingItemDecoration.setDrawAfter { it == adapter.itemCount - 1 }
        recycler.addItemDecoration(paddingItemDecoration)
    }
}