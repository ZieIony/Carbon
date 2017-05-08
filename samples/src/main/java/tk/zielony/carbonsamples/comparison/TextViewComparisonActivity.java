package tk.zielony.carbonsamples.comparison;

import android.os.Bundle;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class TextViewComparisonActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textviewcomparison);

        Samples.initToolbar(this, getString(R.string.textViewComparisonActivity_title));
    }
}
