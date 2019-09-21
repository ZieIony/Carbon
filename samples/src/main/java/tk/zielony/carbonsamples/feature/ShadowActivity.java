package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

@ActivityAnnotation(layout = R.layout.activity_shadow, title = R.string.shadowActivity_title)
public class ShadowActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);
    }
}
