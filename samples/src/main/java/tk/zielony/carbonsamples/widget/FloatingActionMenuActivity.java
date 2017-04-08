package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import carbon.widget.FloatingActionButton;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class FloatingActionMenuActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floatingactionmenu);

        Samples.initToolbar(this, getString(R.string.floatingActionMenuActivity_title));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setMenu(R.menu.menu_fab);
    }
}
