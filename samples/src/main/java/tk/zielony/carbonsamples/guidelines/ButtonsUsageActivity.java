package tk.zielony.carbonsamples.guidelines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import carbon.widget.ProgressBar;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

public class ButtonsUsageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonsusage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);

        ProgressBar progress = findViewById(R.id.progress);
        progress.setProgress(0.8f);
    }
}
