package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.demo.AutoCompleteActivity;
import tk.zielony.carbonsamples.demo.PowerMenuActivity;
import tk.zielony.carbonsamples.demo.QuickReturnActivity;
import tk.zielony.carbonsamples.demo.SearchToolbarActivity;
import tk.zielony.carbonsamples.demo.ShareToolbarActivity;

@ActivityAnnotation(title = R.string.demosActivity_title)
public class DemosActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular problem solutions and other fun ideas implemented with Carbon's widgets and features",
                new SampleActivityItem(PowerMenuActivity.class),
                new SampleActivityItem(AutoCompleteActivity.class, 0, false),
                new SampleActivityItem(QuickReturnActivity.class, 0, false),
                new SampleActivityItem(SearchToolbarActivity.class, 0, false),
                new SampleActivityItem(ShareToolbarActivity.class, 0, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
