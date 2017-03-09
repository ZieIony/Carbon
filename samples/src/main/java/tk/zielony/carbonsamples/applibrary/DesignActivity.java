package tk.zielony.carbonsamples.applibrary;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

/**
 * Created by Marcin on 2015-12-30.
 */
public class DesignActivity extends SamplesActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Design Support Library");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
}
