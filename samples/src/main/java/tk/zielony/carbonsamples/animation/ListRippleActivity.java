package tk.zielony.carbonsamples.animation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ListRippleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ripple);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        String[] items = new String[]{"Blueberry", "Lime", "Lemon", "Orange", "Strawberry"};
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ListRippleAdapter(items));
    }

}
