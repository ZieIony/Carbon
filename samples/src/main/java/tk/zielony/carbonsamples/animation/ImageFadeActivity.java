package tk.zielony.carbonsamples.animation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.DataContext;
import tk.zielony.randomdata.common.DrawableImageGenerator;

public class ImageFadeActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefade);

        Samples.initToolbar(this, getString(R.string.imageFadeActivity_title));

        FrameLayout grid = (FrameLayout) findViewById(R.id.grid);

        DrawableImageGenerator imageGenerator = new DrawableImageGenerator(this);
        DataContext dataContext = new DataContext();
        for (int i = 0; i < grid.getChildCount(); i++) {
            ImageView imageView = (ImageView) grid.getChildAt(i);
            imageView.setImageDrawable(imageGenerator.next(dataContext));
        }

        Handler handler = new Handler();

        findViewById(R.id.button).setOnClickListener(view -> {
            for (int i = 0; i < grid.getChildCount(); i++) {
                ImageView imageView = (ImageView) grid.getChildAt(i);
                handler.postDelayed(() -> {
                    imageView.animateVisibility(imageView.isVisible() ? View.INVISIBLE : View.VISIBLE);
                }, i * 50);
            }
        });
    }
}
