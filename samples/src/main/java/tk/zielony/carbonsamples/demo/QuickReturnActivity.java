package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.widget.FloatingActionButton;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.applibrary.FruitAdapter;

/**
 * Created by Marcin on 2015-07-19.
 */
public class QuickReturnActivity extends Activity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickreturn);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new FruitAdapter(fruits, null));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView.addOnScrollListener(new android.support.v7.widget.RecyclerView.OnScrollListener() {
            int yscroll = 0;

            @Override
            public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab.getVisibility() == View.VISIBLE && fab.getAnimator() == null && yscroll > 50 * Carbon.getDip(getApplicationContext())) {
                    yscroll = 0;
                    fab.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                }
                if (fab.getVisibility() != View.VISIBLE && fab.getAnimator() == null && yscroll < -50 * Carbon.getDip(getApplicationContext())) {
                    yscroll = 0;
                    fab.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
                if (Math.signum(dy) != Math.signum(yscroll))
                    yscroll = 0;
                yscroll += dy;
            }
        });
    }
}
