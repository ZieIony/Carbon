package tk.zielony.carbonsamples.component

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.*
import carbon.recycler.RowListAdapter
import kotlinx.android.synthetic.main.activity_listcomponent.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.Target
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_listcomponent, titleId = R.string.avatarTextListItemActivity_title)
class AvatarTextListItemActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val items = listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DividerItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DividerItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DividerItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                DefaultAvatarTextItem(),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)))

        val randomData = RandomData().apply {
            addGenerator(Drawable::class.java, DrawableAvatarGenerator(this@AvatarTextListItemActivity))
            addGenerator(String::class.java, StringNameGenerator().withMatcher { f: Target -> f.name == "text" })
        }
        randomData.fill(items)

        val adapter = RowListAdapter<Serializable>().apply {
            putFactory(DefaultAvatarTextItem::class.java, { AvatarTextRow(it) })
            putFactory(PaddingItem::class.java, { PaddingRow(it) })
            putFactory(DividerItem::class.java, { DividerRow(it) })
        }
        adapter.items = items

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}