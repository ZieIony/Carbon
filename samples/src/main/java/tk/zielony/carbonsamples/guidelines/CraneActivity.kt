package tk.zielony.carbonsamples.guidelines

import android.os.Bundle
import carbon.component.DefaultImageTextSubtextItem
import carbon.component.ImageTextSubtextRow
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import kotlinx.android.synthetic.main.activity_crane.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.DrawableImageGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.place.StringCityGenerator

@ActivityAnnotation(layout = R.layout.activity_crane, title = R.string.craneActivity_title)
class CraneActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crane_search.setOnClickListener { crane_backdropLayout.toggleLayout() }

        val randomData = RandomData()
        randomData.addGenerators(arrayOf(DrawableImageGenerator(this), StringCityGenerator().withMatcher { f -> f.name == "text" }, TextGenerator().withMatcher { f -> f.name == "subtext" }))
        val items = randomData.generateList(DefaultImageTextSubtextItem::class.java, 4)

        crane_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val adapter = RowListAdapter(DefaultImageTextSubtextItem::class.java, RowFactory<DefaultImageTextSubtextItem> { ImageTextSubtextRow(it) })
        crane_recycler.adapter = adapter
        adapter.items = items
    }
}