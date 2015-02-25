package tk.zielony.carbonsamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import carbon.widget.MaterialListAdapter;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.applibrary.CalculatorActivity;
import tk.zielony.carbonsamples.applibrary.PicassoActivity;
import tk.zielony.carbonsamples.applibrary.RecyclerCardsActivity;


public class AppsLibrariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Handler handler = new Handler();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText("Apps & Libraries");

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{"Calculator", "Picasso", "RecyclerView & cards"
        };
        final Class[] activities = new Class[]{CalculatorActivity.class, PicassoActivity.class, RecyclerCardsActivity.class
        };
        final boolean[] beta = new boolean[]{true, false, false
        };
        listView.setAdapter(new MaterialListAdapter(new MainListAdapter(items, beta)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AppsLibrariesActivity.this, activities[position]));
            }
        });
    }

}
