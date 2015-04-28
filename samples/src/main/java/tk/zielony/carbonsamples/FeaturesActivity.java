package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.feature.LargeShadowActivity;
import tk.zielony.carbonsamples.feature.RobotoActivity;
import tk.zielony.carbonsamples.feature.RoundedCornersActivity;
import tk.zielony.carbonsamples.feature.TextAppearanceActivity;
import tk.zielony.carbonsamples.feature.ZOrderActivity;


public class FeaturesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText("Features");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(RobotoActivity.class, "Roboto"),
                new ViewModel(LargeShadowActivity.class, "Large shadow"),
                new ViewModel(TextAppearanceActivity.class, "Text appearance"),
                new ViewModel(ZOrderActivity.class, "Z order"),
                new ViewModel(RoundedCornersActivity.class, "Rounded corners")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
