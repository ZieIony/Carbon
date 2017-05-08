package tk.zielony.carbonsamples.animation;

import android.os.Bundle;
import android.view.View;

import carbon.widget.FloatingActionButton;
import carbon.widget.FrameLayout;
import carbon.widget.rx.Button;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class WidgetAnimationsActivity extends SamplesActivity {
    int fabVisibility = View.VISIBLE;
    int buttonVisibility = View.VISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        Samples.initToolbar(this, getString(R.string.widgetAnimationsActivity_title));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button button = (Button) findViewById(R.id.button);
        button.clicks().subscribe(view -> {
            if (fabVisibility != View.VISIBLE) {
                fab.animateVisibility(View.VISIBLE);
                fabVisibility = View.VISIBLE;
            } else {
                fab.animateVisibility(View.INVISIBLE);
                fabVisibility = View.INVISIBLE;
            }
        });

        final Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.clicks().subscribe(view -> {
            if (buttonVisibility != View.VISIBLE) {
                button2.animateVisibility(View.VISIBLE);
                buttonVisibility = View.VISIBLE;
            } else {
                button2.animateVisibility(View.INVISIBLE);
                buttonVisibility = View.INVISIBLE;
            }
        });

        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        Button reveal = (Button) findViewById(R.id.reveal);
        reveal.clicks().subscribe(v -> frame.createCircularReveal(20, 20, 0, (float) Math.sqrt((frame.getWidth() - 20) * (frame.getWidth() - 20) + (frame.getHeight() -
                20) * (frame.getHeight() - 20))).start());
    }
}
