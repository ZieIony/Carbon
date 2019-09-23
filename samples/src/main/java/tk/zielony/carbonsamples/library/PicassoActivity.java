package tk.zielony.carbonsamples.library;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import carbon.widget.rx.Button;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_picasso, title = R.string.picassoActivity_title)
public class PicassoActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        final PicassoView image = findViewById(R.id.image);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Picasso.get()
                    .load("http://lorempixel.com/" + image.getWidth() + "/" + image.getHeight() + "/people/#" + System.currentTimeMillis())
                    .placeholder(new ColorDrawable(0x00000000))
                    .error(new ColorDrawable(0x00000000))
                    .into((Target) image);
        });
    }
}
