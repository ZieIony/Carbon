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
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CircularProgressActivity;
import tk.zielony.carbonsamples.widget.DialogActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.SVGActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;


public class WidgetsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText("Widgets");

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{
                "Buttons", "Dialog", "Circular progress", "Progress bars", "Snackbar", "SVG icons", "Text fields", "Tabs"
        };
        final Class[] activities = new Class[]{
                ButtonsActivity.class, DialogActivity.class, CircularProgressActivity.class, ProgressBarsActivity.class,
                SnackbarActivity.class, SVGActivity.class, TextFieldsActivity.class, TabsActivity.class
        };
        final boolean[] beta = new boolean[]{
                false, true, false, true, false, false, true, false
        };
        listView.setAdapter(new MaterialListAdapter(new MainListAdapter(items, beta)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(WidgetsActivity.this, activities[position]));
            }
        });
    }

}
