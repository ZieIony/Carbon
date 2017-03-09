package tk.zielony.carbonsamples.feature;

import tk.zielony.carbonsamples.SamplesActivity;
import android.content.Context;
import android.os.Bundle;

import carbon.CarbonContextWrapper;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-06-29.
 */
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
