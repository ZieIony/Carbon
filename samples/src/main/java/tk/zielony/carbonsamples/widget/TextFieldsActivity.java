package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import carbon.widget.CheckBox;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;

/**
 * Created by Marcin on 2014-12-15.
 */
public class TextFieldsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textfields);

        Samples.initToolbar(this,getString(R.string.textFieldsActivity_title));
    }
}
