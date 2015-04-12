package carbon.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-12-04.
 */
public class TransitionLayout extends android.widget.FrameLayout {
    private static final int DEFAULT_DURATION = 600;
    float radius = 0;
    float x, y;
    private Animator.AnimatorListener listener;
    private Animator.AnimatorListener internalListener;
    Paint paint = new Paint();
    private Bitmap texture;
    private Canvas textureCanvas;
    private ValueAnimator animator;
    private TransitionType currentTransition;
    private int currentIndex = 0;
    private boolean inAnimation;

    public static enum TransitionType {
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
        paint.setAntiAlias(true);

        internalListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                currentIndex++;
                currentIndex %= getChildCount();
                currentTransition = null;
            }
        };

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

    public void startTransition(TransitionType transitionType) {
        startTransition(transitionType, true, DEFAULT_DURATION);
    }

    public void startTransition(TransitionType transitionType, boolean in) {
        startTransition(transitionType, in, DEFAULT_DURATION);
    }

    public void startTransition(TransitionType transitionType, boolean in, int duration) {
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
        float dist = FloatMath.sqrt(x * x + y * y);
        dist = Math.max(dist, FloatMath.sqrt((getWidth() - x) * (getWidth() - x) + y * y));
        dist = Math.max(dist, FloatMath.sqrt(x * x + (getHeight() - y) * (getHeight() - y)));
        dist = Math.max(dist, FloatMath.sqrt((getWidth() - x) * (getWidth() - x) + (getHeight() - y) * (getHeight() - y)));
        animator = ValueAnimator.ofFloat(inAnimation ? 0 : dist, inAnimation ? dist : 0);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
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

        texture = null;
        texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

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
        if (currentTransition == TransitionType.Radial && textureCanvas != null) {
            int bottomView = inAnimation ? currentIndex : (currentIndex + 1) % getChildCount();
            int topView = !inAnimation ? currentIndex : (currentIndex + 1) % getChildCount();

            drawChild(canvas, getChildAt(bottomView), getDrawingTime());
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            getChildAt(topView).draw(textureCanvas);

            RectF rect = new RectF();
            rect.bottom = getHeight();
            rect.right = getWidth();
            canvas.drawCircle(x, y, radius, paint);
        } else if (currentTransition == TransitionType.Fade) {
            int bottomView = inAnimation ? currentIndex : (currentIndex + 1) % getChildCount();
            int topView = !inAnimation ? currentIndex : (currentIndex + 1) % getChildCount();

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

    public void setListener(Animator.AnimatorListener listener) {
        this.listener = listener;
    }
}
