package tk.zielony.carbonsamples.graph

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import carbon.beta.ChartView
import carbon.drawable.ColorStateListFactory
import kotlinx.android.synthetic.main.activity_barchart.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.DataContext
import tk.zielony.randomdata.Generator
import tk.zielony.randomdata.Matcher
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.FloatGenerator
import tk.zielony.randomdata.common.IntegerRGBColorGenerator
import tk.zielony.randomdata.food.StringFruitGenerator


class ColorGenerator(val context: Context) : Generator<ColorStateList>() {
    var colorGenerator = IntegerRGBColorGenerator()
    override fun nextInternal(dataContext: DataContext?): ColorStateList? {
        return ColorStateListFactory.makeControlPrimary(context)
    }

    override fun getDefaultMatcher(): Matcher {
        return Matcher { f -> f.name == "color" }
    }

}

@ActivityAnnotation(layout = R.layout.activity_barchart, title = R.string.barChartActivity_title)
class BarChartActivity : ThemedActivity() {
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
