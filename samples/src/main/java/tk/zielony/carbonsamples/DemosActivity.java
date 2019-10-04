package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.demo.AutoCompleteActivity;
import tk.zielony.carbonsamples.demo.PowerMenuActivity;
import tk.zielony.carbonsamples.demo.QuickReturnActivity;
import tk.zielony.carbonsamples.demo.SearchToolbarActivity;
import tk.zielony.carbonsamples.demo.ShareToolbarActivity;

@ActivityAnnotation(title = R.string.demosActivity_title, layout = R.layout.activity_samplelist)
public class DemosActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular problem solutions and other fun ideas implemented with Carbon's widgets and features",
                new SampleActivityItem(PowerMenuActivity.class, R.drawable.ic_power_settings_new_black_24dp),
                new SampleActivityItem(AutoCompleteActivity.class, 0, true),
                new SampleActivityItem(QuickReturnActivity.class),
                new SampleActivityItem(SearchToolbarActivity.class, R.drawable.carbon_search),
                new SampleActivityItem(ShareToolbarActivity.class, R.drawable.ic_share_black_24dp, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
