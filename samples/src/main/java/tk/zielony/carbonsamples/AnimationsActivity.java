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
import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.ListRippleActivity;
import tk.zielony.carbonsamples.animation.RadialTransitionActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.ScrollViewActivity;


public class AnimationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText("Animations");

        ListView listView = (ListView) findViewById(R.id.list);
        String[] items = new String[]{
                "Widget animations", "Image fade", "List ripple", "Radial transition", "Touch ripple", "ScrollView"
        };
        final Class[] activities = new Class[]{
                tk.zielony.carbonsamples.animation.AnimationsActivity.class, ImageFadeActivity.class, ListRippleActivity.class, RadialTransitionActivity.class,
                RippleActivity.class, ScrollViewActivity.class
        };
        final boolean[] beta = new boolean[]{
                false, false, false, false, false, true
        };
        listView.setAdapter(new MaterialListAdapter(new MainListAdapter(items, beta)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AnimationsActivity.this, activities[position]));
            }
        });
    }

}
