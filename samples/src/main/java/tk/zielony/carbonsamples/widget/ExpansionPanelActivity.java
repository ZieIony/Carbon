package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class ExpansionPanelActivity extends SamplesActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansionpanel);

        Samples.initToolbar(this, getString(R.string.expansionPanelActivity_title));
    }
}
