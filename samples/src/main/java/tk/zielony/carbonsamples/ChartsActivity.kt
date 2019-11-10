package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.graph.BarChartActivity
import tk.zielony.carbonsamples.graph.LineChartActivity

@ActivityAnnotation(title = R.string.chartsActivity_title, layout = R.layout.activity_samplelist)
class ChartsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Charts",
                SampleActivityItem(BarChartActivity::class.java, 0, true),
                SampleActivityItem(LineChartActivity::class.java, 0, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}

