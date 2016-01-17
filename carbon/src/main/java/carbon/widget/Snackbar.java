package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2015-01-07.
 */
public class Snackbar extends android.widget.FrameLayout implements GestureDetector.OnGestureListener {
    public static int INFINITE = -1;
    private float swipe;
    private ValueAnimator animator;
    private View pushedView;
    GestureDetector gestureDetector = new GestureDetector(this);

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        boolean hit = e2.getY() > content.getTop() && getParent() != null;

        if (hit && swipeToDismiss && animator == null) {
            swipe = e2.getX() - e1.getX();
            ViewHelper.setTranslationX(content, swipe);
            ViewHelper.setAlpha(content, Math.max(0, 1 - 2 * Math.abs(swipe) / content.getWidth()));
            postInvalidate();
            if (Math.abs(swipe) > content.getWidth() / 4) {
                handler.removeCallbacks(hideRunnable);
                animator = ObjectAnimator.ofFloat(swipe, content.getWidth() / 2.0f * Math.signum(swipe));
                animator.setDuration(200);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float s = (Float) valueAnimator.getAnimatedValue();
                        ViewHelper.setTranslationX(content, s);
                        float alpha = Math.max(0, 1 - 2 * Math.abs((Float) valueAnimator.getAnimatedValue()) / content.getWidth());
                        ViewHelper.setAlpha(content, alpha);
                        postInvalidate();
                    }
                });
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideInternal();
                        animator = null;
                    }
                });
                if (pushedView != null) {
                    ValueAnimator animator = ValueAnimator.ofFloat(-1, 0);
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            MarginLayoutParams lp = (MarginLayoutParams) content.getLayoutParams();
                            ViewHelper.setTranslationY(pushedView, (content.getHeight() + lp.bottomMargin) * (Float) valueAnimator.getAnimatedValue());
                            if (pushedView.getParent() != null)
                                ((View) pushedView.getParent()).postInvalidate();
                        }
                    });
                    animator.start();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Deprecated
    public interface OnDismissedListener {
        void onDismissed();
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    private TextView message;
    private Button button;
    private Style style;
    private long duration;
    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private Handler handler;
    private View content;
    OnDismissListener onDismissListener;
    boolean swipeToDismiss = true, tapOutsideToDismiss = true;

    static List<Snackbar> next = new ArrayList<>();

    public enum Style {
        Floating, Docked
    }

    public Snackbar(Context context) {
        super(context);
        initSnackbar(R.attr.carbon_snackbarTheme);
    }

    public Snackbar(Context context, String message, String action, int duration) {
        super(context);
        initSnackbar(R.attr.carbon_snackbarTheme);
        setMessage(message);
        setAction(action);
        setDuration(duration);
    }

    private void initSnackbar(int defStyleAttr) {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(defStyleAttr, outValue, true);
        int theme = outValue.resourceId;
        Context themedContext = new ContextThemeWrapper(getContext(), theme);

        content = inflate(themedContext, R.layout.carbon_snackbar, null);
        addView(content);

        message = (TextView) findViewById(R.id.carbon_messageText);
        button = (Button) findViewById(R.id.carbon_actionButton);

        handler = new Handler();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void show(final View view) {
        pushedView = view;
        if (this.getParent() != null)
            return;
        synchronized (Snackbar.class) {
            if (!next.contains(this))
                next.add(this);
            if (next.indexOf(this) == 0) {
                View decor = ((Activity) getContext()).getWindow().getDecorView();
                ((ViewGroup) decor.findViewById(android.R.id.content)).addView(this,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ViewHelper.setAlpha(content, 0);
                AnimUtils.flyIn(content, null);
                if (view != null) {
                    ValueAnimator animator = ValueAnimator.ofFloat(0, -1);
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            MarginLayoutParams lp = (MarginLayoutParams) content.getLayoutParams();
                            ViewHelper.setTranslationY(view, (content.getHeight() + lp.bottomMargin) * (Float) valueAnimator.getAnimatedValue());
                            if (pushedView.getParent() != null)
                                ((View) pushedView.getParent()).postInvalidate();
                        }
                    });
                    animator.start();
                }
                if (duration != INFINITE)
                    handler.postDelayed(hideRunnable, duration);
            }
        }
    }

    public void show() {
        show(pushedView);
    }

    public static void clearQueue() {
        next.clear();
    }

    public void hide() {
        synchronized (Snackbar.class) {
            if (getParent() == null)
                return;
            handler.removeCallbacks(hideRunnable);
            if (onDismissListener != null)
                onDismissListener.onDismiss();
            AnimUtils.flyOut(content, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    hideInternal();
                }
            });
            if (pushedView != null) {
                ValueAnimator animator = ValueAnimator.ofFloat(-1, 0);
                animator.setDuration(200);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        MarginLayoutParams lp = (MarginLayoutParams) content.getLayoutParams();
                        ViewHelper.setTranslationY(pushedView, (content.getHeight() + lp.bottomMargin) * (Float) valueAnimator.getAnimatedValue());
                        if (pushedView.getParent() != null)
                            ((View) pushedView.getParent()).postInvalidate();
                    }
                });
                animator.start();
            }
        }
    }

    private void hideInternal() {
        synchronized (Snackbar.class) {
            if (getParent() != null)
                ((ViewGroup) getParent()).removeView(this);
            if (next.contains(this))
                next.remove(this);
            if (next.size() != 0)
                next.get(0).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (gestureDetector.onTouchEvent(ev))
            return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean hit = event.getY() > content.getTop() && getParent() != null;
        if (tapOutsideToDismiss && !hit) {
            hide();
            return false;
        }

        if (hit && swipeToDismiss) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                swipe = 0;
                handler.removeCallbacks(hideRunnable);
                if (animator != null) {
                    animator.cancel();
                    animator = null;
                    swipe = ViewHelper.getTranslationX(content);
                }
                super.onTouchEvent(event);
                return true;
            } else if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && animator == null) {
                animator = ObjectAnimator.ofFloat(swipe, 0);
                animator.setDuration(200);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float s = (Float) animation.getAnimatedValue();
                        ViewHelper.setTranslationX(content, s);
                        ViewHelper.setAlpha(content, Math.max(0, 1 - 2 * Math.abs(s) / content.getWidth()));
                        postInvalidate();
                    }
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
                super.onTouchEvent(event);
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        button.setOnClickListener(l);
    }

    public void setAction(String action) {
        if (action != null) {
            button.setText(action);
            button.setVisibility(View.VISIBLE);
            content.setPadding(content.getPaddingLeft(), 0, (int) getResources().getDimension(R.dimen.carbon_paddingHalf), 0);
        } else {
            content.setPadding(content.getPaddingLeft(), 0, content.getPaddingLeft(), 0);
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

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) content.getLayoutParams();
        if (style == Style.Floating) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int margin = (int) getResources().getDimension(R.dimen.carbon_padding);
            layoutParams.setMargins(margin, 0, margin, margin);
            layoutParams.gravity = Gravity.START | Gravity.BOTTOM;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.BOTTOM;
        }
        content.setLayoutParams(layoutParams);
        requestLayout();
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
    }

    public boolean isTapOutsideToDismissEnabled() {
        return tapOutsideToDismiss;
    }

    public void setTapOutsideToDismissEnabled(boolean tapOutsideToDismiss) {
        this.tapOutsideToDismiss = tapOutsideToDismiss;
    }

    @Deprecated
    public void setOnDismissedListener(final OnDismissedListener onDismissedListener) {
        this.onDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissedListener != null)
                    onDismissedListener.onDismissed();
            }
        };
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
