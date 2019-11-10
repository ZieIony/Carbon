package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(
        layout = R.layout.activity_textfields,
        title = R.string.textFieldsActivity_title,
        icon = R.drawable.ic_text_fields_black_24dp
)
public class TextFieldsActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
    }
}
