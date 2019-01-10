package carbon.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import carbon.R;
import carbon.widget.TextView;

public class ProgressDialog extends DialogBase {
    private TextView textView;

    public ProgressDialog(@NonNull Context context) {
        super(context);
        initProgressDialog();
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initProgressDialog();
    }

    private void initProgressDialog() {
        setContentView(R.layout.carbon_progressdialog);
        textView = findViewById(R.id.carbon_progressDialogText);
        setMinimumWidth(0);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }

    public void setText(CharSequence text) {
        textView.setText(text);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
            setMinimumWidth(0);
        } else {
            textView.setVisibility(View.VISIBLE);
            setMinimumWidth(getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dialogMinimumWidth));
        }
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dialogPadding);
        getContentView().setPadding(padding, 0, padding, hasButtons() ? 0 : padding);
    }

    @Override
    protected void setButton(String text, View.OnClickListener listener, int buttonId) {
        super.setButton(text, listener, buttonId);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dialogPadding);
        getContentView().setPadding(padding, hasTitle() ? 0 : padding, padding, 0);
    }
}
