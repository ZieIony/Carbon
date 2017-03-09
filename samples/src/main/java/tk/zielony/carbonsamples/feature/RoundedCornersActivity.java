package tk.zielony.carbonsamples.feature;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class RoundedCornersActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_corners);

        Target view = (Target) findViewById(R.id.image);

        Picasso.with(this).load(R.drawable.mazda).into(view);
    }
}
