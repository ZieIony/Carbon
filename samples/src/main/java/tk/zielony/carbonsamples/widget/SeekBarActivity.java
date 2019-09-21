package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

@ActivityAnnotation(layout = R.layout.activity_seekbar, title = R.string.seekBarActivity_title)
public class SeekBarActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);
    }
}
