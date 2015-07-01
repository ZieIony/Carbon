package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;

import carbon.CarbonContextWrapper;
import carbon.widget.NavigationView;
import carbon.widget.Toolbar;
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

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setIconVisible(true);
        //toolbar.setText("NavigationView");

        navigationView = (NavigationView) findViewById(R.id.drawerMenu);
        navigationView.setMenu(R.menu.menu_navigation);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CarbonContextWrapper(newBase));
    }
}
