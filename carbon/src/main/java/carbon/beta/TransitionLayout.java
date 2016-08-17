package carbon.beta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import carbon.animation.AnimUtils;
import carbon.widget.FrameLayout;

/**
 * Created by Marcin on 2014-12-04.
 */
public class TransitionLayout extends FrameLayout {
    public static final int DEFAULT_DURATION = 500;
    float radius = 0;
    float x, y;
    private Animator.AnimatorListener listener;
    private Animator.AnimatorListener internalListener;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueAnimator animator;
    private TransitionType currentTransition;
    private int currentIndex = 0, nextIndex = 0;
    private boolean inAnimation;

    Path radialMask;
    private static PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public enum TransitionType {
        Radial, Fade
    }

    public TransitionLayout(Context context) {
        this(context, null);
    }

    public TransitionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransitionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint.setColor(0xffffffff);

        internalListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                currentIndex = nextIndex;
                currentTransition = null;
            }
        };

        radialMask = new Path();
        radialMask.setFillType(Path.FillType.INVERSE_WINDING);

        setClipChildren(false);
    }

    public void setHotspot(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setHotspot(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int[] location2 = new int[2];
        getLocationOnScreen(location2);
        this.x = location[0] - location2[0] + view.getWidth() / 2;
        this.y = location[1] - location2[1] + view.getHeight() / 2;
    }

    public void startTransition(int toChild) {
        startTransition(toChild, TransitionType.Radial, DEFAULT_DURATION, true);
    }

    public void startTransition(int toChild, TransitionType transitionType) {
        startTransition(toChild, transitionType, DEFAULT_DURATION, true);
    }

    public void startTransition(int toChild, TransitionType transitionType, int duration) {
        startTransition(toChild, transitionType, duration, true);
    }

    public void startTransition(int toChild, TransitionType transitionType, int duration, boolean in) {
        nextIndex = toChild;
        inAnimation = in;
        currentTransition = transitionType;
        switch (transitionType) {
            case Radial:
                startRadialTransition(duration);
                break;
            case Fade:
                startFadeTransition(duration);
                break;
        }
    }

    private void startRadialTransition(int duration) {
        float dist = (float) Math.sqrt(x * x + y * y);
        dist = (float) Math.max(dist, Math.sqrt((getWidth() - x) * (getWidth() - x) + y * y));
        dist = (float) Math.max(dist, Math.sqrt(x * x + (getHeight() - y) * (getHeight() - y)));
        dist = (float) Math.max(dist, Math.sqrt((getWidth() - x) * (getWidth() - x) + (getHeight() - y) * (getHeight() - y)));
        animator = ValueAnimator.ofFloat(inAnimation ? 0 : dist, inAnimation ? dist : 0);
        animator.setDuration(duration);
        animator.setInterpolator(inAnimation ? new AccelerateInterpolator() : new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (Float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(internalListener);
        if (listener != null)
            animator.addListener(listener);

        animator.start();
    }

    private void startFadeTransition(int duration) {
        AnimUtils.fadeOut(this, null);
        animator = ValueAnimator.ofFloat(inAnimation ? 0 : 1, inAnimation ? 1 : 0);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int topView = !inAnimation ? currentIndex : (currentIndex + 1) % getChildCount();
                ViewHelper.setAlpha(getChildAt(topView), (Float) animation.getAnimatedValue());
                postInvalidate();
            }
        });
        animator.addListener(internalListener);
        if (listener != null)
            animator.addListener(listener);
        animator.start();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int bottomView = inAnimation ? currentIndex : nextIndex;
        int topView = !inAnimation ? currentIndex : nextIndex;

        if (currentTransition == TransitionType.Radial) {
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            float r = (float) (radius / Math.sqrt(2));
            canvas.clipRect(x - r, y - r, x + r, y + r, Region.Op.DIFFERENCE);
            drawChild(canvas, getChildAt(bottomView), getDrawingTime());
            canvas.restoreToCount(saveCount);

            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            canvas.clipRect(x - radius, y - radius, x + radius, y + radius);
            drawChild(canvas, getChildAt(topView), getDrawingTime());

            paint.setXfermode(pdMode);
            radialMask.reset();
            radialMask.addCircle(x, y, Math.max(radius, 1), Path.Direction.CW);
            canvas.drawPath(radialMask, paint);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else if (currentTransition == TransitionType.Fade) {
            drawChild(canvas, getChildAt(bottomView), getDrawingTime());
            drawChild(canvas, getChildAt(topView), getDrawingTime());
        } else {
            drawChild(canvas, getChildAt(currentIndex), getDrawingTime());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return getChildAt(getCurrentChild()).dispatchTouchEvent(ev);
    }

    public void setListener(Animator.AnimatorListener listener) {
        this.listener = listener;
    }

    public int getCurrentChild() {
        return currentIndex;
    }

    public void setCurrentChild(int index) {
        currentIndex = index;
        postInvalidate();
    }
}
