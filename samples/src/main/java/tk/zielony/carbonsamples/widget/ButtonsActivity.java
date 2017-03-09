package tk.zielony.carbonsamples.widget;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ButtonsActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        Samples.initToolbar(this, getString(R.string.buttonsActivity_title));
    }
}
