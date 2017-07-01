package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.animation.AnimUtils;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

public class Snackbar {

    public interface OnActionListener {
        void onAction();
    }

    public interface OnDismissedListener {
        void onDismiss();
    }

    public enum Style {
        Floating, Docked, Auto
    }

    public static int INFINITE = -1;

    private Context context;
    private ViewGroup container;

    private Snackbar.Style style = null;
    private long duration;
    private Runnable hideRunnable = this::dismiss;
    private Handler handler;
    private Snackbar.OnDismissedListener onDismissedListener;
    private boolean swipeToDismiss = true;
    private boolean tapOutsideToDismiss;
    private int gravity = Gravity.START | Gravity.BOTTOM;

    private SnackbarLayout snackbarLayout;

    private static List<Snackbar> next = new ArrayList<>();

    public Snackbar(Context context) {
        this.context = context;
        handler = new Handler();
    }

    public Snackbar(Context context, String message, int duration) {
        this.context = context;
        handler = new Handler();
        snackbarLayout = new SnackbarLayout(context);
        snackbarLayout.getView().setMessage(message);
        setDuration(duration);
        setTapOutsideToDismissEnabled(false);
    }

    public void show(final ViewGroup container) {
        synchronized (SnackbarLayout.class) {
            this.container = container;
            if (!next.contains(this))
                next.add(this);
            if (next.indexOf(this) == 0) {
                SnackbarView snackbarView = snackbarLayout.getView();
                Rect windowFrame = new Rect();
                container.getWindowVisibleDisplayFrame(windowFrame);
                Rect drawingRect = new Rect();
                container.getDrawingRect(drawingRect);
                //setPaddingBottom(0, 0, 0, drawingRect.bottom - windowFrame.bottom);
                if (style == null)
                    setStyle(Style.Auto);
                if (snackbarView.getInAnimator() == null)
                    snackbarView.setInAnimator(AnimUtils.getSlideInAnimator());
                if (snackbarView.getOutAnimator() == null)
                    snackbarView.setOutAnimator(AnimUtils.getSlideOutAnimator(gravity));
                container.addView(snackbarLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                snackbarView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
                if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                    snackbarView.setTranslationY(snackbarView.getMeasuredHeight() + layoutParams.bottomMargin);
                } else {
                    snackbarView.setTranslationY(-snackbarView.getMeasuredHeight() - layoutParams.topMargin);
                }
                snackbarView.setVisibility(INVISIBLE);
                snackbarView.animateVisibility(View.VISIBLE);
                if (duration != INFINITE)
                    handler.postDelayed(hideRunnable, duration);
            }
        }
    }

    public void show() {
        show(container);
    }

    public static void clearQueue() {
        next.clear();
    }

    public void dismiss() {
        synchronized (SnackbarLayout.class) {
            handler.removeCallbacks(hideRunnable);
            SnackbarView snackbarView = snackbarLayout.getView();
            snackbarView.getOutAnimator().addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    fireOnDismissedListener();
                    hideInternal();
                }
            });
            snackbarView.animateVisibility(GONE);
        }
    }

    private void fireOnDismissedListener() {
        if (onDismissedListener != null)
            onDismissedListener.onDismiss();
    }

    private void hideInternal() {
        synchronized (SnackbarLayout.class) {
            if (snackbarLayout.getParent() == null)
                return;
            ((ViewGroup) snackbarLayout.getParent()).removeView(snackbarLayout);
            if (next.contains(this))
                next.remove(this);
            if (next.size() != 0)
                next.get(0).show();
        }
    }

    public Snackbar.Style getStyle() {
        return style;
    }

    public void setStyle(Snackbar.Style style) {
        this.style = style;
        SnackbarView snackbarView = snackbarLayout.getView();
        if (style == Snackbar.Style.Auto)
            this.style = context.getResources().getBoolean(R.bool.carbon_isPhone) ? Snackbar.Style.Docked : Snackbar.Style.Floating;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = snackbarLayout.generateDefaultLayoutParams();
        if (style == Snackbar.Style.Floating) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int margin = (int) context.getResources().getDimension(R.dimen.carbon_margin);
            layoutParams.setMargins(margin, margin, margin, margin);
            layoutParams.gravity = gravity;
            snackbarView.setCornerRadius((int) context.getResources().getDimension(R.dimen.carbon_cornerRadiusButton));
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = gravity;
            snackbarView.setCornerRadius(0);
        }
        snackbarView.setLayoutParams(layoutParams);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isSwipeToDismissEnabled() {
        return swipeToDismiss;
    }

    public void setSwipeToDismissEnabled(boolean swipeToDismiss) {
        this.swipeToDismiss = swipeToDismiss;
        snackbarLayout.initSwipeToDismissEnabled();
    }

    public boolean isTapOutsideToDismissEnabled() {
        return tapOutsideToDismiss;
    }

    public void setTapOutsideToDismissEnabled(boolean tapOutsideToDismiss) {
        this.tapOutsideToDismiss = tapOutsideToDismiss;
    }

    public void setOnDismissedListener(Snackbar.OnDismissedListener onDismissedListener) {
        this.onDismissedListener = onDismissedListener;
    }

    public void setInAnimator(Animator inAnim) {
        snackbarLayout.getView().setInAnimator(inAnim);
    }

    public Animator getInAnimator() {
        return snackbarLayout.getView().getInAnimator();
    }

    public void setOutAnimator(Animator outAnim) {
        snackbarLayout.getView().setOutAnimator(outAnim);
    }

    public Animator getOutAnimator() {
        return snackbarLayout.getView().getOutAnimator();
    }

    public View getView() {
        return snackbarLayout.getView();
    }

    public void setAction(String text, OnActionListener listener) {
        snackbarLayout.getView().setAction(text, listener);
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        SnackbarView snackbarView = snackbarLayout.getView();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = snackbarLayout.generateDefaultLayoutParams();
        layoutParams.gravity = gravity;
        snackbarView.setLayoutParams(layoutParams);
    }

    public int getGravity() {
        return gravity;
    }

    class SnackbarLayout extends FrameLayout {

        private float swipe;
        private ValueAnimator animator;
        private SnackbarView snackbarView;
        private Rect rect = new Rect();
        private Handler handler;

        GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (swipeToDismiss && animator == null && getParent() != null) {
                    swipe = e2.getX() - e1.getX();
                    snackbarView.setTranslationX(swipe);
                    snackbarView.setAlpha(Math.max(0, 1 - 2 * Math.abs(swipe) / snackbarView.getMeasuredWidth()));
                    postInvalidate();
                    if (Math.abs(swipe) > snackbarView.getMeasuredWidth() / 4) {
                        handler.removeCallbacks(hideRunnable);
                        animator = ObjectAnimator.ofFloat(swipe, snackbarView.getMeasuredWidth() / 2.0f * Math.signum(swipe));
                        animator.setDuration(200);
                        animator.addUpdateListener(valueAnimator -> {
                            float s = (Float) valueAnimator.getAnimatedValue();
                            snackbarView.setTranslationX(s);
                            float alpha = Math.max(0, 1 - 2 * Math.abs((Float) valueAnimator.getAnimatedValue()) / snackbarView.getMeasuredWidth());
                            snackbarView.setAlpha(alpha);
                            postInvalidate();
                        });
                        animator.start();
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                hideInternal();
                                animator = null;
                            }
                        });
                    }
                    return true;
                }
                return false;
            }

        });

        @SuppressLint("ClickableViewAccessibility")
        private OnTouchListener listener = (v, event) -> {
            if (isSwipeToDismissEnabled()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    swipe = 0;
                    handler.removeCallbacks(hideRunnable);
                    if (animator != null) {
                        animator.cancel();
                        animator = null;
                        swipe = snackbarView.getTranslationX();
                    }
                    return true;
                } else if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && animator == null) {
                    animator = ObjectAnimator.ofFloat(swipe, 0);
                    animator.setDuration(200);
                    animator.addUpdateListener(animation -> {
                        float s = (Float) animation.getAnimatedValue();
                        snackbarView.setTranslationX(s);
                        snackbarView.setAlpha(Math.max(0, 1 - 2 * Math.abs(s) / snackbarView.getWidth()));
                        postInvalidate();
                    });
                    animator.start();
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animator.cancel();
                            animator = null;
                            if (duration != INFINITE)
                                handler.postDelayed(hideRunnable, duration);
                        }
                    });
                    return true;
                }
            }

            return false;
        };

        public SnackbarLayout(Context context) {
            super(context);
            handler = new Handler();
            snackbarView = new SnackbarView(context);
            addView(snackbarView);
        }

        @Override
        public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
            snackbarView.getHitRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                if (gestureDetector.onTouchEvent(event))
                    return true;
            } else if (isTapOutsideToDismissEnabled()) {
                dismiss();
            }
            return super.dispatchTouchEvent(event);
        }

        public void initSwipeToDismissEnabled() {
            snackbarView.setOnTouchListener(swipeToDismiss ? listener : null);
        }

        public SnackbarView getView() {
            return snackbarView;
        }
    }

}
