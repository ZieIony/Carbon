package tk.zielony.carbonsamples.applibrary;

/**
 * Created by Marcin on 2015-03-30.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;
import carbon.widget.RecyclerView.OnItemClickedListener;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

public class DrawerActivity extends Activity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    private DrawerLayout drawerLayout;
    private LinearLayout drawerMenu;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText(R.string.app_name);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerMenu = (LinearLayout) findViewById(R.id.drawerMenu);
        RecyclerView drawerList = (RecyclerView) findViewById(R.id.drawerList);
        drawerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FruitAdapter adapter = new FruitAdapter(fruits);
        drawerList.setAdapter(adapter);
        adapter.setOnItemClickedListener(new OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                setTitle(fruits.get(position));
                drawerLayout.closeDrawer(drawerMenu);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setText(title.toString());
    }

}