package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ToolbarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
    }
}
