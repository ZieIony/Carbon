package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2015-01-07.
 */
public class Snackbar extends FrameLayout implements AnimatedView, GestureDetector.OnGestureListener {
    public interface OnDismissedListener {
        void OnDismissed();
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
    OnDismissedListener onDismissedListener;
    boolean swipeToDismiss = true, tapOutsideToDismiss = false;

    static List<Snackbar> next = new ArrayList<>();

    GestureDetector detector = new GestureDetector(this);

    public enum Style {
        Floating, Docked
    }

    public Snackbar(Context context) {
        super(context);
        init(null, R.attr.carbon_snackbarStyle);
    }

    public Snackbar(Context context, String message, String action, int duration) {
        super(context);
        init(null, R.attr.carbon_snackbarStyle);
        setMessage(message);
        setAction(action);
        setDuration(duration);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        content = inflate(getContext(), R.layout.carbon_snackbar, null);
        addView(content);

        message = (TextView) findViewById(R.id.carbon_messageText);
        button = (Button) findViewById(R.id.carbon_actionButton);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Snackbar, defStyleAttr, 0);
        style = Style.values()[a.getInt(R.styleable.Snackbar_carbon_layoutStyle, 0)];
        setStyle(style);
        Carbon.initAnimations(this, attrs, defStyleAttr);

        duration = a.getInt(R.styleable.Snackbar_carbon_duration, 0);

        a.recycle();

        handler = new Handler();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void show() {
        synchronized (Snackbar.class) {
            View decor = ((Activity) getContext()).getWindow().getDecorView();
            ((ViewGroup) decor.findViewById(android.R.id.content)).addView(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            if (!next.contains(this))
                next.add(this);
            if (next.indexOf(this) == 0) {
                ViewHelper.setAlpha(content, 0);
                AnimUtils.animateIn(content, getInAnimation(), null);
                if (duration > 0)
                    handler.postDelayed(hideRunnable, duration);
            }
        }
    }

    public void hide() {
        handler.removeCallbacks(hideRunnable);
        AnimUtils.animateOut(content, getOutAnimation(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                synchronized (Snackbar.class) {
                    ((ViewGroup) getParent()).removeView(Snackbar.this);
                    next.remove(Snackbar.this);
                    if (next.size() != 0)
                        next.get(0).show();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeToDismiss) {
            if (detector.onTouchEvent(event))
                return true;
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
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) content.getLayoutParams();
        if (style == Style.Floating) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int margin = (int) getResources().getDimension(R.dimen.carbon_padding);
            layoutParams.setMargins(margin, margin, margin, margin);
            layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
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
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    private void fireOnDismissed() {
        if (onDismissedListener != null)
            onDismissedListener.OnDismissed();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > 5) {
            hide();
            fireOnDismissed();
        }
        return true;
    }
}
