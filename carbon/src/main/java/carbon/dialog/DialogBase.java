package carbon.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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
            dividerCallback(contentView.getHeight());
        });
        container.addView(view);
    }

    protected void initLayout() {
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

    protected void dividerCallback(int contentHeight) {
        if (container.getChildCount() == 0 || container.getChildAt(0).getHeight() <= contentHeight) {
            if (topDivider != null)
                topDivider.setVisibility(View.GONE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.GONE);
        } else {
            if (topDivider != null)
                topDivider.setVisibility(View.VISIBLE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        if (titleTextView == null && title != null) {
            container.addView(getLayoutInflater().inflate(R.layout.carbon_dialogheader, null), 0);
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

    public void setNegativeButton(String text, View.OnClickListener listener) {
        setButton(text, listener, R.id.carbon_buttonNegative);
    }

    public void setPositiveButton(String text, View.OnClickListener listener) {
        setButton(text, listener, R.id.carbon_buttonPositive);
    }

    private void setButton(String text, View.OnClickListener listener, int buttonId) {
        if (buttonContainer == null) {
            container.addView(getLayoutInflater().inflate(R.layout.carbon_dialogfooter, null));
            buttonContainer = dialogLayout.findViewById(R.id.carbon_buttonContainer);
            bottomDivider = dialogLayout.findViewById(R.id.carbon_bottomDivider);
        }
        Button button = findViewById(buttonId);
        button.setText(text);
        button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
            dismiss();
        });
        button.setVisibility(View.VISIBLE);
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
}
