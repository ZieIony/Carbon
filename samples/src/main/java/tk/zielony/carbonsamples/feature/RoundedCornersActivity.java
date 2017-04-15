package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import carbon.widget.ImageView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.common.DrawableImageGenerator;

public class RoundedCornersActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_corners);

        Samples.initToolbar(this, getString(R.string.roundedCornersActivity_title));

        ImageView view = (ImageView) findViewById(R.id.image);
        view.setImageDrawable(new DrawableImageGenerator(this).next(null));
    }
}
