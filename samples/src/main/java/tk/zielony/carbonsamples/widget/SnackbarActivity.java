package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.CheckBox;
import carbon.widget.FloatingActionButton;
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

        final CheckBox tapCheckBox = (CheckBox) findViewById(R.id.tap);
        final CheckBox swipeCheckBox = (CheckBox) findViewById(R.id.swipe);
        final CheckBox floatingCheckBox = (CheckBox) findViewById(R.id.floating);
        final CheckBox infiniteCheckBox = (CheckBox) findViewById(R.id.infinite);
        final CheckBox pushCheckBox = (CheckBox) findViewById(R.id.push);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = new Snackbar(SnackbarActivity.this, "Hello world!", "dismiss", infiniteCheckBox.isChecked() ? Snackbar.INFINITE : getResources().getInteger(R.integer.carbon_snackbarDuration));
                snackbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setStyle(floatingCheckBox.isChecked() ? Snackbar.Style.Floating : Snackbar.Style.Docked);
                snackbar.setTapOutsideToDismissEnabled(tapCheckBox.isChecked());
                snackbar.setSwipeToDismissEnabled(swipeCheckBox.isChecked());
                if (pushCheckBox.isChecked())
                    snackbar.addPushedView(fab);
                snackbar.show((ViewGroup) getWindow().getDecorView().getRootView());
                snackbar.setOnDismissListener(new Snackbar.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                });
            }
        });

        Snackbar.clearQueue();
    }
}
