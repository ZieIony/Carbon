package tk.zielony.carbonsamples.animation;

import android.os.Bundle;
import android.view.View;

import carbon.widget.Button;
import carbon.widget.FloatingActionButton;
import carbon.widget.FrameLayout;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_animations, titleId = R.string.widgetAnimationsActivity_title)
public class WidgetAnimationsActivity extends ThemedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        final FloatingActionButton fab = findViewById(R.id.fab);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> fab.animateVisibility(fab.isVisible() ? View.INVISIBLE : View.VISIBLE));

        final Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> button2.animateVisibility(button2.isVisible() ? View.INVISIBLE : View.VISIBLE));

        FrameLayout frame = findViewById(R.id.frame);
        Button reveal = findViewById(R.id.reveal);
        reveal.setOnClickListener(v -> {
            float radius = (float) Math.sqrt((frame.getWidth() - 20) * (frame.getWidth() - 20) + (frame.getHeight() - 20) * (frame.getHeight() - 20));
            frame.createCircularReveal(20, 20, 0, radius).start();
        });
    }
}
