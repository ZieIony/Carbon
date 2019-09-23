package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import carbon.dialog.Dialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_simpledialog, title = R.string.simpleDialogActivity_title)
public class SimpleDialogActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        EditText titleText = findViewById(R.id.titleText);
        EditText buttonText = findViewById(R.id.buttonText);

        findViewById(R.id.showDialog).setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_text);
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            if (buttonText.length() > 0)
                dialog.setPositiveButton(buttonText.getText().toString(), null);
            dialog.show();
        });
    }
}
