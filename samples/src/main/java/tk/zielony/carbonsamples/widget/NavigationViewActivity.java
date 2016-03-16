package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.NavigationView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class NavigationViewActivity extends Activity {
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationview);

        navigationView = (NavigationView) findViewById(R.id.drawerMenu);
        navigationView.setMenu(R.menu.menu_navigation);
        navigationView.setHeader(R.layout.header_scrollview);
    }
}
