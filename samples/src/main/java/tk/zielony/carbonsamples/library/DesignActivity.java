package tk.zielony.carbonsamples.library;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class DesignActivity extends SamplesActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Design Support Library");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
}
