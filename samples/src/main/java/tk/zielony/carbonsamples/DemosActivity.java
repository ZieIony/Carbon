package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.demo.AutoCompleteActivity;
import tk.zielony.carbonsamples.demo.PowerMenuActivity;
import tk.zielony.carbonsamples.demo.QuickReturnActivity;
import tk.zielony.carbonsamples.demo.SearchToolbarActivity;
import tk.zielony.carbonsamples.demo.ShareToolbarActivity;


public class DemosActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.demosActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular problem solutions and other fun ideas implemented with Carbon's widgets and features",
                new SampleActivityItem(PowerMenuActivity.class, getString(R.string.powerMenuActivity_title)),
                new SampleActivityItem(AutoCompleteActivity.class, getString(R.string.autoCompleteActivity_title), false),
                new SampleActivityItem(QuickReturnActivity.class, getString(R.string.quickReturnActivity_title), false),
                new SampleActivityItem(SearchToolbarActivity.class, getString(R.string.searchToolbarActivity_title), false),
                new SampleActivityItem(ShareToolbarActivity.class, getString(R.string.shareToolbarActivity_title), true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
