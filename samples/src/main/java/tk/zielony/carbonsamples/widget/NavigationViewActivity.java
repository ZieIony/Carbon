package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import carbon.widget.NavigationView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class NavigationViewActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationview);

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawerMenu);
        navigationView.setMenu(R.menu.menu_navigation);
        navigationView.setHeader(R.layout.header_scrollview);
    }
}
