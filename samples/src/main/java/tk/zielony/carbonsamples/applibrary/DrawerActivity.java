package tk.zielony.carbonsamples.applibrary;

/**
 * Created by Marcin on 2015-03-30.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;

import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

public class DrawerActivity extends Activity {
    private static String[] fruits = new String[]{
            "Lime", "Lemon", "Orange", "Strawberry", "Blueberry", "Plum"
    };

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

        drawerList.setAdapter(new FruitAdapter(fruits, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setTitle(fruits[i]);
                drawerLayout.closeDrawer(drawerMenu);
            }
        }));
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setText(title.toString());
    }

}