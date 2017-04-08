package tk.zielony.carbonsamples.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class HtmlActivity extends SamplesActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        Samples.initToolbar(this, getString(R.string.htmlActivity_title));
    }
}
