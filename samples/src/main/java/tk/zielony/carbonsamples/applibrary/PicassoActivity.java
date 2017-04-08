package tk.zielony.carbonsamples.applibrary;

import android.os.Bundle;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class PicassoActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);

        Samples.initToolbar(this, getString(R.string.picassoActivity_title));

        final PicassoView image = (PicassoView) findViewById(R.id.image);
        findViewById(R.id.button).setOnClickListener(view -> {
            image.setVisibility(View.INVISIBLE);
            Picasso.with(PicassoActivity.this).load("http://lorempixel.com/400/500/people/#" + System.currentTimeMillis()).into((Target) image);
        });
    }
}
