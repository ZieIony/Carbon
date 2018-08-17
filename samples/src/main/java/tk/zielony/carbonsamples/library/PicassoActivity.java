package tk.zielony.carbonsamples.library;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import carbon.widget.rx.Button;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class PicassoActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);

        Samples.initToolbar(this, getString(R.string.picassoActivity_title));

        final PicassoView image = findViewById(R.id.image);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Picasso.with(PicassoActivity.this)
                    .load("http://lorempixel.com/" + image.getWidth() + "/" + image.getHeight() + "/people/#" + System.currentTimeMillis())
                    .placeholder(new ColorDrawable(0x00000000))
                    .error(new ColorDrawable(0x00000000))
                    .into((Target) image);
        });
    }
}
