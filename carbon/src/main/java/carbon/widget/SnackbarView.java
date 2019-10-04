package carbon.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;

import androidx.annotation.Nullable;

import carbon.R;

class SnackbarView extends LinearLayout {
    private TextView message;
    private Button button;

    public SnackbarView(Context context) {
        super(getThemedContext(context), null, R.attr.carbon_snackbarStyle);
        initSnackbar();
    }

    static Context getThemedContext(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.carbon_snackbarTheme, outValue, true);
        int theme = outValue.resourceId;
        return new ContextThemeWrapper(context, theme);
    }

    private void initSnackbar() {
        View.inflate(getContext(), R.layout.carbon_snackbar, this);

        message = findViewById(R.id.carbon_messageText);
        button = findViewById(R.id.carbon_actionButton);
    }


    public void setAction(String text, @Nullable Snackbar.OnActionListener listener) {
        if (text != null) {
            button.setText(text);
            button.setVisibility(View.VISIBLE);
            setPadding(getPaddingLeft(), 0, (int) getResources().getDimension(R.dimen.carbon_paddingHalf), 0);
            button.setOnClickListener(v -> {
                if (listener != null)
                    listener.onAction();
            });
        } else {
            setPadding(getPaddingLeft(), 0, getPaddingLeft(), 0);
            button.setVisibility(View.GONE);
        }
    }

    public String getAction() {
        return button.getText().toString();
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public String getMessage() {
        return message.getText().toString();
    }

}
