package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import carbon.dialog.Dialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class SimpleDialogActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpledialog);

        Samples.initToolbar(this, getString(R.string.simpleDialogActivity_title));

        EditText titleText = (EditText) findViewById(R.id.titleText);
        EditText buttonText = (EditText) findViewById(R.id.buttonText);

        findViewById(R.id.button).setOnClickListener(view -> {
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
