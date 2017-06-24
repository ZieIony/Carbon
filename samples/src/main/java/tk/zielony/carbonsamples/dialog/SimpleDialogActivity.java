package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import carbon.dialog.Dialog;
import carbon.dialog.ProgressDialog;
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

        findViewById(R.id.showProgressDialog).setOnClickListener(view -> {
            ProgressDialog dialog = new ProgressDialog(this, R.style.AppTheme);
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            dialog.setText("Working...");
            dialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(dialog::dismiss, 2000);
        });
    }
}
