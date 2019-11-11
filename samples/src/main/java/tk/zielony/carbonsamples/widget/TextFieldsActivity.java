package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(
        layoutId = R.layout.activity_textfields,
        titleId = R.string.textFieldsActivity_title,
        iconId = R.drawable.ic_text_fields_black_24dp
)
public class TextFieldsActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
    }
}
