package tk.zielony.carbonsamples;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.demo.AutoCompleteActivity;
import tk.zielony.carbonsamples.demo.PowerMenuActivity;
import tk.zielony.carbonsamples.demo.QuickReturnActivity;
import tk.zielony.carbonsamples.demo.SearchToolbarActivity;
import tk.zielony.carbonsamples.demo.ShareToolbarActivity;


public class DemosActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Samples.initToolbar(this, getString(R.string.demosActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(PowerMenuActivity.class, getString(R.string.powerMenuActivity_title)),
                new ViewModel(AutoCompleteActivity.class, getString(R.string.autoCompleteActivity_title), false),
                new ViewModel(QuickReturnActivity.class, getString(R.string.quickReturenActivity_title), false),
                new ViewModel(SearchToolbarActivity.class, getString(R.string.searchToolbarActivity_title), false),
                new ViewModel(ShareToolbarActivity.class, getString(R.string.shareToolbarActivity_title), true)
        };
        recyclerView.setLayoutManager(getResources().getBoolean(R.bool.tablet) ?
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false) :
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
