package carbon.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import carbon.R;
import carbon.widget.TextView;

public class ProgressDialog extends DialogBase {
    public ProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.carbon_progressdialog);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.carbon_progressdialog);
    }

    public void setText(String text) {
        TextView textView = findViewById(R.id.carbon_progressDialogText);
        textView.setText(text);
    }
}
