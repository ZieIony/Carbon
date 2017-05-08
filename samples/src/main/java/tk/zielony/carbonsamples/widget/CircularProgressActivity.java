package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import carbon.widget.ProgressBar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class CircularProgressActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_progress);

        Samples.initToolbar(this, getString(R.string.circularProgressActivity_title));

        final ProgressBar progress1 = (ProgressBar) findViewById(R.id.progress1);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress1.setProgress((float) (progress1.getProgress() + Math.random() / 100));
                if (progress1.getProgress() < 1) {
                    handler.postDelayed(this, 10);
                } else {
                    progress1.setVisibility(View.INVISIBLE);
                }
            }
        };
        findViewById(R.id.button1).setOnClickListener(view -> progress1.animateVisibility(progress1.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        final ProgressBar progress2 = (ProgressBar) findViewById(R.id.progress2);
        findViewById(R.id.button2).setOnClickListener(view -> progress2.animateVisibility(progress2.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        final ProgressBar progress3 = (ProgressBar) findViewById(R.id.progress3);
        findViewById(R.id.button3).setOnClickListener(view -> progress3.animateVisibility(progress3.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        final ProgressBar progress4 = (ProgressBar) findViewById(R.id.progress4);
        findViewById(R.id.button4).setOnClickListener(view -> progress4.animateVisibility(progress4.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        handler.postDelayed(runnable, 10);
    }
}
