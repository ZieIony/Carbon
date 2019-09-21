package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import carbon.widget.ImageView;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.common.DrawableImageGenerator;

@ActivityAnnotation(layout = R.layout.activity_rounded_corners, title = R.string.roundedCornersActivity_title)
public class RoundedCornersActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        ImageView view = findViewById(R.id.image);
        view.setImageDrawable(new DrawableImageGenerator(this).next(null));
    }
}
