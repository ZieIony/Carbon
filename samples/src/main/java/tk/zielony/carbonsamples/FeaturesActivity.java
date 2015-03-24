package tk.zielony.carbonsamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.feature.LargeShadowActivity;
import tk.zielony.carbonsamples.feature.RobotoActivity;
import tk.zielony.carbonsamples.feature.SaveStateActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.feature.StatusNavigationActivity;
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

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{
                "Roboto", "Saving state", "Large shadow", "Animated shadow", "Status & navigation bars", "Text appearance", "Z order"
        };
        final Class[] activities = new Class[]{
                RobotoActivity.class, SaveStateActivity.class, LargeShadowActivity.class, ShadowActivity.class, StatusNavigationActivity.class, TextAppearanceActivity.class, ZOrderActivity.class
        };
        final boolean[] beta = new boolean[]{
                false, true, false, false, false, false, false
        };
        listView.setAdapter(new MainListAdapter(items, beta));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(FeaturesActivity.this, activities[position]));
            }
        });
    }

}
