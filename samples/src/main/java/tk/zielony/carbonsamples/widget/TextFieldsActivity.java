package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import carbon.widget.CheckBox;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class TextFieldsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textfields);

        final View editText = findViewById(R.id.editText);
        final View editText2 = findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);

        CheckBox checkBox = (CheckBox) findViewById(R.id.enabled);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editText.setEnabled(isChecked);
                editText2.setEnabled(isChecked);
                editText3.setEnabled(isChecked);
            }
        });
    }
}
