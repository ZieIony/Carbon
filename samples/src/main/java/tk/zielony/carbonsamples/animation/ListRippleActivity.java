package tk.zielony.carbonsamples.animation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ListRippleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ripple);

        ListView listView = (ListView) findViewById(R.id.list);

        String[] items = new String[]{"Blueberry", "Lime", "Lemon", "Orange", "Strawberry"};
        listView.setAdapter(new ListRippleAdapter(items));
    }

}
