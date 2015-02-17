package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.EditText;
import carbon.widget.OnValidateListener;
import carbon.beta.ValidatedEditText;

/**
 * Created by Marcin on 2014-12-15.
 */
public class TextFieldsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textfields);

        EditText editText = (EditText) findViewById(R.id.editText);
    }
}
