package tk.zielony.carbonsamples.graph

import android.content.res.ColorStateList
import android.os.Bundle
import carbon.beta.ChartView
import kotlinx.android.synthetic.main.activity_barchart.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.FloatGenerator
import tk.zielony.randomdata.food.StringFruitGenerator


@SampleAnnotation(layoutId = R.layout.activity_linechart, titleId = R.string.lineChartActivity_title)
class LineChartActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val randomData = RandomData()
        randomData.addGenerator(String::class.java, StringFruitGenerator())
        randomData.addGenerator(Float::class.java, FloatGenerator(0f, 100f).withMatcher { field -> field.name == "value" })
        randomData.addGenerator(ColorStateList::class.java, ColorGenerator(this))

        val items = randomData.generateArray(ChartView.Item::class.java, 10)

        chart.setItems(items)
    }
}
