package tk.zielony.carbonsamples.feature;

import android.content.Context;
import android.os.Bundle;

import carbon.CarbonContextWrapper;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class ContextWrapperActivity extends SamplesActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contextwrapper);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CarbonContextWrapper(newBase));
    }
}
