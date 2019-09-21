package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.guidelines.BasilActivity;
import tk.zielony.carbonsamples.guidelines.ButtonsUsageActivity;
import tk.zielony.carbonsamples.guidelines.CraneActivity;
import tk.zielony.carbonsamples.guidelines.MenusBehaviorActivity;
import tk.zielony.carbonsamples.guidelines.ShrineActivity;

@ActivityAnnotation(title = R.string.guidelinesActivity_title)
public class GuidelinesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Sample screens taken from Material Design guidelines",
                new SampleActivityItem(ButtonsUsageActivity.class),
                new SampleActivityItem(MenusBehaviorActivity.class),
                new SampleActivityItem(ShrineActivity.class, true),
                new SampleActivityItem(CraneActivity.class),
                new SampleActivityItem(BasilActivity.class, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

