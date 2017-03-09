package tk.zielony.carbonsamples.animation;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;
import android.view.View;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-01-22.
 */
public class ImageFadeActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefade);

        final View cow = findViewById(R.id.mazda);
        findViewById(R.id.button).setOnClickListener(view -> cow.setVisibility(cow.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
    }
}
