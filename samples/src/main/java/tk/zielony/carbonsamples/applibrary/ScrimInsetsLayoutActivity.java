package tk.zielony.carbonsamples.applibrary;

/**
 * Created by Marcin on 2015-03-30.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import carbon.widget.FrameLayout;
import carbon.widget.ListView;
import carbon.widget.TextView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

public class ScrimInsetsLayoutActivity extends Activity {
    private DrawerLayout drawerLayout;
    private ViewGroup drawerMenu;
    private static String[] fruits = new String[]{
            "Lime", "Lemon", "Orange", "Strawberry", "Blueberry", "Plum"
    };
    private static int[] colors = new int[]{
            0xffA5FF00, 0xffFFE900, 0xffFF9900, 0xffFF1000, 0xff6E00FF, 0xffCF60FF
    };
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FruitFragment extends Fragment {
        private int color;

        public FruitFragment() {

        }

        public FruitFragment(int color) {
            this.color = color;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            FrameLayout layout = new FrameLayout(container.getContext());
            layout.setBackgroundColor(color);
            return layout;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scriminsetslayout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(0x00000000);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText(R.string.app_name);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setStatusBarBackgroundColor(0xffffffff);
        drawerMenu = (ViewGroup) findViewById(R.id.drawerMenu);
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
                View view = View.inflate(ScrimInsetsLayoutActivity.this, R.layout.row_drawer, null);
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
                getFragmentManager().beginTransaction().disallowAddToBackStack().replace(R.id.fragment,new FruitFragment(colors[i])).commit();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setText(title.toString());
    }

}