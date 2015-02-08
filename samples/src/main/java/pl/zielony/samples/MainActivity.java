package pl.zielony.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pl.zielony.carbon.widget.MaterialListAdapter;
import pl.zielony.carbon.widget.Toolbar;


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
        String[] items = new String[]{"Picasso & Carbon", "Buttons", "ListView with ripple", "Large shadow", "Roboto", "Text appearance",
                "SVG icons", "Status and navigation bars", "View animations", "Snackbar",
                "TextFields", "Z order", "Saving state", "Dialog", "Image fade", "Radial transition", "Progress bar"
        };
        final Class[] activities = new Class[]{PicassoActivity.class, ButtonsActivity.class, ListRippleActivity.class,
                ShadowActivity.class, RobotoActivity.class, TextAppearanceActivity.class, SVGActivity.class, StatusNavigationActivity.class,
                AnimationsActivity.class, SnackbarActivity.class, TextFieldsActivity.class, ZOrderActivity.class,
                SaveStateActivity.class, DialogActivity.class, ImageFadeActivity.class, RadialTransitionActivity.class,
                ProgressActivity.class
        };
        final boolean[] beta = new boolean[]{false, false, true,
                false, false, false, false, false,
                false, false, true, false,
                true, true, false, false,
                false
        };
        listView.setAdapter(new MaterialListAdapter(new MainListAdapter(items, beta)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, activities[position]));
            }
        });
    }

}
