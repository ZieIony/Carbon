package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import carbon.widget.Snackbar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class SnackbarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = new Snackbar(SnackbarActivity.this, "Hello world!", "dismiss", getResources().getInteger(R.integer.carbon_snackbarDuration));
                snackbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.hide();
                    }
                });
                snackbar.setStyle(Snackbar.Style.Floating);
                snackbar.show();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = new Snackbar(SnackbarActivity.this, "Hello world!", "dismiss", getResources().getInteger(R.integer.carbon_snackbarDuration));
                snackbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.hide();
                    }
                });
                snackbar.setStyle(Snackbar.Style.Docked);
                snackbar.show();
            }
        });
    }
}
