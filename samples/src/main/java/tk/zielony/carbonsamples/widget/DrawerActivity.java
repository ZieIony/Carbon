package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.carbonsamples.library.FruitAdapter;

public class DrawerActivity extends SamplesActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    private DrawerLayout drawerLayout;
    private LinearLayout drawerMenu;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerMenu = (LinearLayout) findViewById(R.id.drawerMenu);
        RecyclerView drawerList = (RecyclerView) findViewById(R.id.drawerList);
        drawerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FruitAdapter adapter = new FruitAdapter(fruits, null);
        drawerList.setAdapter(adapter);
        adapter.setOnItemClickedListener((view, item, position) -> {
            setTitle(item);
            drawerLayout.closeDrawer(drawerMenu);
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setTitle(title.toString());
    }

}