package tk.zielony.carbonsamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import carbon.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText(R.string.app_name);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.findViewById(R.id.icon1).setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.findViewById(R.id.icon2).setVisibility(View.GONE);
                    }
                }, 100);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{
                "Animations", "Apps & Libraries", "Features", "Widgets"
        };
        final Class[] activities = new Class[]{
                AnimationsActivity.class, AppsLibrariesActivity.class, FeaturesActivity.class, WidgetsActivity.class
        };
        final boolean[] beta = new boolean[]{
                false, false, false, false
        };
        listView.setAdapter(new MainListAdapter(items, beta));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, activities[position]));
            }
        });
    }

}
