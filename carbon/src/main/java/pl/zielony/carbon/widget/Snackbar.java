package pl.zielony.carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;

import pl.zielony.carbon.R;
import pl.zielony.carbon.animation.AnimUtils;
import pl.zielony.carbon.animation.DefaultAnimatorListener;
import pl.zielony.carbon.internal.PopupWindow;

/**
 * Created by Marcin on 2015-01-07.
 */
public class Snackbar extends PopupWindow {
    private TextView message;
    private Button button;
    private Style style;
    private AnimUtils.Style inAnim, outAnim;
    private long duration;
    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private Handler handler;
    private View content;

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

    private void init(AttributeSet attrs, int defStyle) {
        content = inflate(getContext(), R.layout.carbon_snackbar, null);
        addView(content);
        ViewHelper.setAlpha(content, 0);

        message = (TextView) findViewById(R.id.carbon_messageText);
        button = (Button) findViewById(R.id.carbon_actionButton);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Snackbar, defStyle, 0);
        style = Style.values()[a.getInt(R.styleable.Snackbar_carbon_layoutStyle, 0)];
        setStyle(style);
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.Snackbar_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.Snackbar_carbon_inAnimation, 0)];

        duration = a.getInt(R.styleable.Snackbar_carbon_duration, 0);

        a.recycle();

        WindowManager.LayoutParams windowParams = (WindowManager.LayoutParams) getLayoutParams();
        windowParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = Gravity.BOTTOM|Gravity.LEFT|Gravity.RIGHT;
        windowParams.horizontalMargin=0;
        windowParams.verticalMargin=0;
        setLayoutParams(windowParams);

        handler = new Handler();
    }

    public void show() {
        super.show();
        AnimUtils.animateIn(content, inAnim, null);
        if (duration > 0)
            handler.postDelayed(hideRunnable, duration);
    }

    public void hide() {
        handler.removeCallbacks(hideRunnable);
        AnimUtils.animateOut(content, outAnim, new DefaultAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                Snackbar.super.hide();
            }
        });
    }

    public void hideImmediate(){
        handler.removeCallbacks(hideRunnable);
        Snackbar.super.hide();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
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
        MarginLayoutParams layoutParams = (MarginLayoutParams) content.getLayoutParams();
        if(style==Style.Floating) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int margin = (int) getResources().getDimension(R.dimen.carbon_padding);
            layoutParams.setMargins(margin, margin, margin, margin);
        }else{
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(0, 0, 0, 0);
        }
        content.setLayoutParams(layoutParams);
        requestLayout();
    }

    public AnimUtils.Style getOutAnimation() {
        return outAnim;
    }

    public void setOutAnimation(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnimation() {
        return inAnim;
    }

    public void setInAnimation(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
