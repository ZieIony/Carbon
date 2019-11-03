package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.graph.BarChartActivity;
import tk.zielony.carbonsamples.graph.LineChartActivity;

@ActivityAnnotation(title = R.string.chartsActivity_title, layout = R.layout.activity_samplelist)
public class ChartsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Charts",
                new SampleActivityItem(BarChartActivity.class, 0, true),
                new SampleActivityItem(LineChartActivity.class, 0, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

