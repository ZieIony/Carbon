package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

/**
 * Created by Marcin on 2014-12-15.
 */
public class TextAppearanceActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_appearance);

        Samples.initToolbar(this, getString(R.string.textappearanceActivity_title));
    }
}
