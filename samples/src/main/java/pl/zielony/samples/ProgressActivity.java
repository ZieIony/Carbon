package pl.zielony.samples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import pl.zielony.carbon.widget.CircularProgress;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ProgressActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        final CircularProgress progress = (CircularProgress) findViewById(R.id.progressbar);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress.setProgress((float) (progress.getProgress() + Math.random() / 100));
                if (progress.getProgress() < 1) {
                    handler.postDelayed(this, 10);
                }else{
                    progress.setVisibility(View.INVISIBLE);
                }
            }
        };
        handler.postDelayed(runnable, 10);
    }
}
