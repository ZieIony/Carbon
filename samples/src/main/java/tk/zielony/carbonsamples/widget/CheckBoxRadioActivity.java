package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import carbon.widget.CheckBox;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;

/**
 * Created by Marcin on 2015-03-06.
 */
public class CheckBoxRadioActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox_radio);

        Samples.initToolbar(this,getString(R.string.checkBoxRadioActivity_title));

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
            }
        });

        findViewById(R.id.uncheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(false);
            }
        });
    }
}
