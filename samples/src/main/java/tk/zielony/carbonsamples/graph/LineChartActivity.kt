package tk.zielony.carbonsamples.graph

import android.os.Bundle
import carbon.beta.ChartView
import kotlinx.android.synthetic.main.activity_barchart.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.FloatGenerator
import tk.zielony.randomdata.food.StringFruitGenerator


@ActivityAnnotation(layout = R.layout.activity_linechart, title = R.string.lineChartActivity_title)
class LineChartActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val randomData = RandomData()
        randomData.addGenerator(StringFruitGenerator())
        randomData.addGenerator(FloatGenerator(0f, 100f).withMatcher { field -> field.name == "value" })
        randomData.addGenerator(ColorGenerator(this))

        val items = randomData.generateArray(ChartView.Item::class.java, 10)

        chart.setItems(items)
    }
}
