package carbon.beta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2014-11-29.
 */
public class BottomSheet extends android.widget.FrameLayout implements AnimatedView {
    private float dim;
    ViewGroup content;

    public BottomSheet(Context context) {
        super(context);
        init();
    }

    public BottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.carbon_bottom_sheet, this);
        content = (ViewGroup) findViewById(R.id.bottomSheetContent);
    }

    public void animateIn() {
        if (getAnimation() != null)
            return;

        ValueAnimation animation = new ValueAnimation(dim, 0.5f, this, "dim");
        animation.setDuration(300);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimation(null);
            }
        });
        startAnimation(animation);

        content.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, content.getMeasuredHeight(), 0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        content.startAnimation(translateAnimation);
    }

    public void animateOut(final Animation.AnimationListener listener) {
        if (getAnimation() != null)
            return;

        ValueAnimation animation = new ValueAnimation(dim, 0, this, "dim");
        animation.setDuration(300);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimation(null);
                if (listener != null)
                    listener.onAnimationEnd(null);
            }
        });
        startAnimation(animation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, content.getMeasuredHeight());
        translateAnimation.setDuration(300);
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        content.startAnimation(translateAnimation);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getVisibility() != View.VISIBLE)
            return false;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (Carbon.dim)
            canvas.drawColor(Color.argb((int) (0xff * dim), 0, 0, 0));
        super.dispatchDraw(canvas);
    }

    public void setDim(float dim) {
        this.dim = dim;
    }

    @Override
    public void addView(View child) {
        content.addView(child);
    }
}
