package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import carbon.widget.NavigationView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class NavigationViewActivity extends SamplesActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationview);

        Samples.initToolbar(this, getString(R.string.navigationViewActivity_title));

        NavigationView drawerMenu = findViewById(R.id.drawerMenu);
        drawerMenu.setMenu(R.menu.menu_navigation);
    }

}