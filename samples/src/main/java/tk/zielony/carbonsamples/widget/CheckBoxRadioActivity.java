package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;

import carbon.drawable.CheckableDrawable;
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

        Samples.initToolbar(this, getString(R.string.checkBoxRadioActivity_title));

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        findViewById(R.id.check).setOnClickListener(view -> checkBox.setChecked(true));

        findViewById(R.id.uncheck).setOnClickListener(view -> checkBox.setChecked(false));

        CheckBox checkBoxGroup = (CheckBox) findViewById(R.id.checkBoxGroup);
        CheckBox checkBoxChild1 = (CheckBox) findViewById(R.id.checkBoxChild1);
        CheckBox checkBoxChild2 = (CheckBox) findViewById(R.id.checkBoxChild2);

        checkBoxGroup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxChild1.setChecked(isChecked);
            checkBoxChild2.setChecked(isChecked);
        });

        CheckBox.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (checkBoxChild1.isChecked() != checkBoxChild2.isChecked()) {
                checkBoxGroup.setChecked(CheckableDrawable.CheckedState.INDETERMINATE);
            } else {
                checkBoxGroup.setChecked(checkBoxChild1.isChecked());
            }
        };
        checkBoxChild1.setOnCheckedChangeListener(listener);
        checkBoxChild2.setOnCheckedChangeListener(listener);
    }
}
