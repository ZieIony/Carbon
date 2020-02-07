package tk.zielony.carbonsamples.component

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.*
import carbon.drawable.VectorDrawable
import carbon.recycler.DividerItemDecoration
import carbon.recycler.RowListAdapter
import carbon.widget.ArraySearchAdapter
import carbon.widget.SearchEditText.OnFilterListener
import kotlinx.android.synthetic.main.activity_listcomponent.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.person.StringNameGenerator
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_listcomponent, titleId = R.string.iconTextListItemActivity_title)
class IconTextListItemActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val adapter = RowListAdapter<Serializable>().apply {
            putFactory(DefaultIconTextItem::class.java, { IconTextRow(it) })
            putFactory(PaddingItem::class.java, { PaddingRow(it) })
            putFactory(DefaultIconSearchItem::class.java, { IconSearchRow<DefaultIconSearchItem>(it!!, ArraySearchAdapter(arrayOf<String>()), OnFilterListener { filterResults: List<Any?>? -> }) })
        }

        val drawable = VectorDrawable(resources, R.raw.ic_face_24px)
        val generator = StringNameGenerator()
        adapter.items = listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                DefaultIconSearchItem(this),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                DefaultIconTextItem(drawable, generator.next()),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)))

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this)
        dividerItemDecoration.setDrawAfter { position: Int -> adapter.getItem(position) is DefaultIconSearchItem }
        recycler.addItemDecoration(dividerItemDecoration)
    }
}