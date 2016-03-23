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
import tk.zielony.carbonsamples.Samples;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ButtonsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        Samples.initToolbar(this,getString(R.string.buttonsActivity_title));
    }
}
