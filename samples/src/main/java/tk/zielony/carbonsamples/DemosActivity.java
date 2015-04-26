package tk.zielony.carbonsamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.demo.PowerMenuActivity;
import tk.zielony.carbonsamples.demo.ShareToolbarActivity;


public class DemosActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText("Demos");

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{
                "Power Menu", "Share Toolbar"
        };
        final Class[] activities = new Class[]{
                PowerMenuActivity.class, ShareToolbarActivity.class
        };
        final boolean[] beta = new boolean[]{
                false, true
        };
        listView.setAdapter(new MainListAdapter(items, beta));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(DemosActivity.this, activities[position]));
            }
        });
    }

}
