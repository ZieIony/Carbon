package tk.zielony.carbonsamples.feature;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;

import com.nineoldandroids.animation.ValueAnimator;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ShadowActivity extends SamplesActivity {
    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
    }
}
