package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import java.util.List;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.CheckBox;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ButtonsActivity extends Activity {
    private boolean debugEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        final LinearLayout contentView = (LinearLayout) findViewById(R.id.content);

        final DebugOverlay overlay = new DebugOverlay(this);

        final ImageView debug = (ImageView) findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!debugEnabled) {
                    debug.setTint(Carbon.getThemeColor(ButtonsActivity.this, R.attr.carbon_iconColor));
                    overlay.show();
                    debugEnabled = true;
                } else {
                    debug.setTint(Carbon.getThemeColor(ButtonsActivity.this, R.attr.colorControlNormal));
                    overlay.dismiss();
                    debugEnabled = false;
                }
            }
        });

        CheckBox checkBox = (CheckBox) findViewById(R.id.enabled);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                List<View> views = contentView.findViewsWithTag("enable");
                for (View v : views)
                    v.setEnabled(b);
            }
        });
    }
}
