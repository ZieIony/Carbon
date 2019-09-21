package tk.zielony.carbonsamples.library

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_recycler_cards.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.DrawableImageGenerator
import tk.zielony.randomdata.common.StringDateGenerator
import java.util.*

@ActivityAnnotation(layout = R.layout.activity_recycler_cards, title = R.string.recyclerCardsActivity_title)
class RecyclerCardsActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val randomData = RandomData()
        randomData.addGenerators(arrayOf(StringDateGenerator(), DrawableImageGenerator(this)))
        val items = arrayOfNulls<ViewModel>(5)
        randomData.fillAsync(items) { recyclerView.adapter = RecyclerAdapter(Arrays.asList<ViewModel>(*items), R.layout.card) }
    }
}
