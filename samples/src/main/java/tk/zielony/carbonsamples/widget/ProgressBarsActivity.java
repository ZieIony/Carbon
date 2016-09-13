package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import carbon.widget.ProgressBar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ProgressBarsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bars);

        final View progress1 = findViewById(R.id.progress5);
        findViewById(R.id.button1).setOnClickListener(view -> progress1.setVisibility(progress1.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        final ProgressBar progress2 = (ProgressBar) findViewById(R.id.progress6);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress2.setProgress((float) (progress2.getProgress() + Math.random() / 100));
                if (progress2.getProgress() < 1) {
                    handler.postDelayed(this, 10);
                } else {
                    progress2.setVisibility(View.INVISIBLE);
                }
            }
        };
        findViewById(R.id.button2).setOnClickListener(view -> progress2.setVisibility(progress2.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        final View progress3 = findViewById(R.id.progress7);
        findViewById(R.id.button3).setOnClickListener(view -> progress3.setVisibility(progress3.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));

        handler.postDelayed(runnable, 10);
    }
}
