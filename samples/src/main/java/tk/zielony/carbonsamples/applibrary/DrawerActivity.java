package tk.zielony.carbonsamples.applibrary;

/**
 * Created by Marcin on 2015-03-30.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import carbon.widget.LinearLayout;
import carbon.widget.ListView;
import carbon.widget.TextView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

public class DrawerActivity extends Activity {
    private DrawerLayout drawerLayout;
    private LinearLayout drawerMenu;
    private static String[] fruits = new String[]{
            "Lime", "Lemon", "Orange", "Strawberry", "Blueberry", "Plum"
    };
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText(R.string.app_name);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerMenu = (LinearLayout) findViewById(R.id.drawerMenu);
        ListView drawerList = (ListView) findViewById(R.id.drawerList);

        drawerList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return fruits.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View convertView, ViewGroup viewGroup) {
                View view = View.inflate(DrawerActivity.this, R.layout.row_drawer, null);
                TextView tv = (TextView) view.findViewById(R.id.text);
                tv.setText(fruits[i]);
                return view;
            }
        });
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setTitle(fruits[i]);
                drawerLayout.closeDrawer(drawerMenu);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setText(title.toString());
    }

}