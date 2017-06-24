package tk.zielony.carbonsamples.animation;

import android.os.Bundle;
import android.view.View;

import carbon.widget.FrameLayout;
import carbon.widget.rx.Button;
import carbon.widget.rx.FloatingActionButton;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class WidgetAnimationsActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        Samples.initToolbar(this, getString(R.string.widgetAnimationsActivity_title));

        final FloatingActionButton fab = findViewById(R.id.fab);
        Button button = findViewById(R.id.button);
        button.clicks().map(v -> fab.isVisible() ? View.INVISIBLE : View.VISIBLE).subscribe(fab.animatedVisibility());

        final Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        button3.clicks().map(v -> button2.isVisible() ? View.INVISIBLE : View.VISIBLE).subscribe(button2.animatedVisibility());

        FrameLayout frame = findViewById(R.id.frame);
        Button reveal = findViewById(R.id.reveal);
        reveal.clicks().subscribe(v -> frame.createCircularReveal(20, 20, 0, (float) Math.sqrt((frame.getWidth() - 20) * (frame.getWidth() - 20) + (frame.getHeight() -
                20) * (frame.getHeight() - 20))).start());
    }
}
