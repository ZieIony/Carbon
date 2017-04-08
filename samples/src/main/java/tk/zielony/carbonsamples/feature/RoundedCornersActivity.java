package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class RoundedCornersActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_corners);

        Samples.initToolbar(this, getString(R.string.roundedCornersActivity_title));

        Target view = (Target) findViewById(R.id.image);

        Picasso.with(this).load(R.drawable.mazda).into(view);
    }
}
