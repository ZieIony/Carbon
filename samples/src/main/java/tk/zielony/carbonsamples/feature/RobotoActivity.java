package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class RobotoActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roboto);

        Samples.initToolbar(this, getString(R.string.robotoActivity_title));
    }
}
