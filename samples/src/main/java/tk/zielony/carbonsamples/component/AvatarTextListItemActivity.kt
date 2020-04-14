package tk.zielony.carbonsamples.component

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.AvatarTextRow
import carbon.component.DefaultAvatarTextItem
import carbon.recycler.RowListAdapter
import carbon.recycler.ViewItemDecoration
import kotlinx.android.synthetic.main.activity_listcomponent.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.Target
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator

@SampleAnnotation(layoutId = R.layout.activity_listcomponent, titleId = R.string.avatarTextListItemActivity_title)
class AvatarTextListItemActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val items = listOf(
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem())

        val randomData = RandomData().apply {
            addGenerator(Drawable::class.java, DrawableAvatarGenerator(this@AvatarTextListItemActivity))
            addGenerator(String::class.java, StringNameGenerator().withMatcher { f: Target -> f.name == "text" })
        }
        randomData.fill(items)

        val adapter = RowListAdapter<DefaultAvatarTextItem>().apply {
            putFactory(DefaultAvatarTextItem::class.java, { AvatarTextRow(it) })
        }
        adapter.items = items

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val paddingItemDecoration = ViewItemDecoration(this, R.layout.carbon_row_padding)
        paddingItemDecoration.setDrawBefore { it == 0 }
        paddingItemDecoration.setDrawAfter { it == adapter.itemCount - 1 }
        recycler.addItemDecoration(paddingItemDecoration)
    }
}