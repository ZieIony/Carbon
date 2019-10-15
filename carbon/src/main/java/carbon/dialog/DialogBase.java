package carbon.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import carbon.Carbon;
import carbon.R;
import carbon.widget.Button;
import carbon.widget.Divider;
import carbon.widget.LinearLayout;
import carbon.widget.TextView;

public abstract class DialogBase extends android.app.Dialog {

    private LinearLayout container;

    private TextView titleTextView;

    private ViewGroup buttonContainer;

    private View dialogLayout;

    protected Divider topDivider;

    protected Divider bottomDivider;

    private View contentView;

    public DialogBase(@NonNull Context context) {
        super(context, Carbon.getThemeResId(context, android.R.attr.dialogTheme));
        initLayout();
    }

    public DialogBase(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initLayout();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null), null);
    }

    @Override
    public void setContentView(@NonNull View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        contentView = view;
        contentView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            onContentHeightChanged(contentView.getHeight());
        });
        container.addView(view);
    }

    public View getContentView() {
        return contentView;
    }

    private void initLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialogLayout = getLayoutInflater().inflate(R.layout.carbon_dialog, null);
        container = dialogLayout.findViewById(R.id.carbon_windowContent);
        super.setContentView(dialogLayout);
    }

    protected void onContentHeightChanged(int contentHeight) {
        if (topDivider != null)
            topDivider.setVisibility(View.GONE);
        if (bottomDivider != null)
            bottomDivider.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        if (titleTextView == null && title != null) {
            container.addView(getLayoutInflater().inflate(R.layout.carbon_dialog_header, null), 0);
            titleTextView = dialogLayout.findViewById(R.id.carbon_windowTitle);
            titleTextView.setText(title);
            topDivider = dialogLayout.findViewById(R.id.carbon_topDivider);
        } else if (titleTextView != null && title == null) {
            container.removeViewAt(0);
            titleTextView = null;
            topDivider = null;
        }
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        setTitle(getContext().getResources().getString(titleId));
    }

    @Deprecated
    public void setNegativeButton(String text, View.OnClickListener listener) {
        addButton(text, listener);
    }

    @Deprecated
    public void setPositiveButton(String text, View.OnClickListener listener) {
        addButton(text, listener);
    }

    public void addButton(String text, View.OnClickListener listener) {
        if (buttonContainer == null) {
            container.addView(getLayoutInflater().inflate(R.layout.carbon_dialog_footer, null));
            buttonContainer = dialogLayout.findViewById(R.id.carbon_buttonContainer);
            bottomDivider = dialogLayout.findViewById(R.id.carbon_bottomDivider);
        }
        Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.carbon_dialog_button, buttonContainer, false);
        button.setText(text);
        button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
            dismiss();
        });
        buttonContainer.addView(button);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        container.requestLayout();
        container.setVisibility(View.INVISIBLE);
        super.show();
        container.animateVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        container.animateVisibility(View.INVISIBLE).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                DialogBase.super.dismiss();
            }
        });
    }

    public boolean hasButtons() {
        return buttonContainer != null;
    }

    public boolean hasTitle() {
        return titleTextView != null;
    }

    public void setMinimumWidth(int minimumWidth) {
        container.setMinimumWidth(minimumWidth);
    }

    public void setMinimumHeight(int minimumHeight) {
        container.setMinimumHeight(minimumHeight);
    }
}
