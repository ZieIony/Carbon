package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;
import carbon.animation.AnimatedView;

public class PopupWindow extends android.widget.PopupWindow {

    private View content;
    private android.view.View anchorView;

    public PopupWindow(Context context) {
        super(android.view.View.inflate(context, R.layout.carbon_popupwindow, null));
        super.getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
    }

    public PopupWindow(View content) {
        this(content.getContext());

        setContentView(content);
    }

    @Override
    public void setContentView(View contentView) {
        if (super.getContentView() == null) {
            super.setContentView(contentView);
        } else {
            content = contentView;
            content.setVisibility(android.view.View.INVISIBLE);
            FrameLayout container = super.getContentView().findViewById(R.id.carbon_popupContainer);
            container.removeAllViews();
            container.addView(content);
        }
    }

    @Override
    public View getContentView() {
        return content == null ? super.getContentView() : content;
    }

    public boolean show(android.view.View anchor, int gravity) {
        anchorView = anchor;

        super.showAtLocation(anchor, gravity, 0, 0);

        update();

        if (content instanceof AnimatedView) {
            ((AnimatedView) content).animateVisibility(android.view.View.VISIBLE);
        }

        return true;
    }

    public boolean showImmediate(android.view.View anchor, int gravity) {
        anchorView = anchor;

        super.showAtLocation(anchor, gravity, 0, 0);

        update();

        return true;
    }

    public void update() {
        if (anchorView == null)
            return;

        super.getContentView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        update(anchorView, 0, 0, super.getContentView().getMeasuredWidth(), super.getContentView().getMeasuredHeight());

        super.update();
    }

    @Override
    public void dismiss() {
        if (content instanceof AnimatedView) {
            Animator animator = ((AnimatedView) content).animateVisibility(android.view.View.INVISIBLE);
            if (animator != null) {
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        PopupWindow.super.dismiss();
                    }
                });
            } else {
                super.dismiss();
            }
        } else {
            super.dismiss();
        }
    }

    public void dismissImmediate() {
        super.dismiss();
    }
}
