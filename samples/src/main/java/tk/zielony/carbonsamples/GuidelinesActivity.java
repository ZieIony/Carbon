package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.guidelines.BasilActivity;
import tk.zielony.carbonsamples.guidelines.ButtonsUsageActivity;
import tk.zielony.carbonsamples.guidelines.CraneActivity;
import tk.zielony.carbonsamples.guidelines.MenusBehaviorActivity;
import tk.zielony.carbonsamples.guidelines.ShrineActivity;

@ActivityAnnotation(title = R.string.guidelinesActivity_title, layout = R.layout.activity_samplelist)
public class GuidelinesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Sample screens taken from Material Design guidelines",
                new SampleActivityItem(ButtonsUsageActivity.class),
                new SampleActivityItem(MenusBehaviorActivity.class),
                new SampleActivityItem(ShrineActivity.class, 0, true),
                new SampleActivityItem(CraneActivity.class),
                new SampleActivityItem(BasilActivity.class, 0, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

