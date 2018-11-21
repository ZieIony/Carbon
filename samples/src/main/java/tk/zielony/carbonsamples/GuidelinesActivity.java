package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.guidelines.BasilActivity;
import tk.zielony.carbonsamples.guidelines.ButtonsUsageActivity;
import tk.zielony.carbonsamples.guidelines.CraneActivity;
import tk.zielony.carbonsamples.guidelines.MenusBehaviorActivity;
import tk.zielony.carbonsamples.guidelines.ShrineActivity;

public class GuidelinesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.guidelinesActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Sample screens taken from Material Design guidelines",
                new SampleActivityItem(ButtonsUsageActivity.class, getString(R.string.buttonsUsageActivity_title)),
                new SampleActivityItem(MenusBehaviorActivity.class, getString(R.string.menusBehaviorActivity_title)),
                new SampleActivityItem(ShrineActivity.class, getString(R.string.shrineActivity_title), true),
                new SampleActivityItem(CraneActivity.class, getString(R.string.craneActivity_title)),
                new SampleActivityItem(BasilActivity.class, getString(R.string.basilActivity_title), true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

