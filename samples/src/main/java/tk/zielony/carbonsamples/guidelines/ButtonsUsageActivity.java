package tk.zielony.carbonsamples.guidelines;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.ProgressBar;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-06-14.
 */
public class ButtonsUsageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonsusage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);

        ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        progress.setProgress(0.8f);
    }
}
