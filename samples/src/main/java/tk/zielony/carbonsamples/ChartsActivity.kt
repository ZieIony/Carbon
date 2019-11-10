package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.graph.BarChartActivity
import tk.zielony.carbonsamples.graph.LineChartActivity

@ActivityAnnotation(
        title = R.string.chartsActivity_title,
        layout = R.layout.activity_samplelist,
        icon = R.drawable.ic_show_chart_black_24dp
)
class ChartsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Charts",
                SampleActivityItem(BarChartActivity::class.java, true),
                SampleActivityItem(LineChartActivity::class.java, true),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}

