package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import carbon.dialog.ProgressDialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class ProgressDialogActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressdialog);

        Samples.initToolbar(this, getString(R.string.progressDialogActivity_title));

        EditText titleText = findViewById(R.id.titleText);
        EditText messageText = findViewById(R.id.messageText);
        EditText buttonText = findViewById(R.id.buttonText);

        findViewById(R.id.showProgressDialog).setOnClickListener(view -> {
            ProgressDialog dialog = new ProgressDialog(this);
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            if (buttonText.length() > 0)
                dialog.setPositiveButton(buttonText.getText().toString(), null);
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable dismissRunnable = dialog::dismiss;
            dialog.setOnDismissListener(dialogInterface -> handler.removeCallbacks(dismissRunnable));
            dialog.setText(messageText.getText());
            dialog.show();
            handler.postDelayed(dialog::dismiss, 2000);
        });
    }
}
