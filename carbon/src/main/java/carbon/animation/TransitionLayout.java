package carbon.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Marcin on 2014-12-04.
 */
public class TransitionLayout extends FrameLayout {
    private static final int DEFAULT_DURATION = 600;
    float radius = 0;
    float x, y;
    private Animator.AnimatorListener listener;
    private Animator.AnimatorListener internalListener;
    Paint paint = new Paint();
    private Bitmap texture;
    private Canvas textureCanvas;
    private ValueAnimator animator;
    private int currentView = 0;
    private boolean inAnimation;

    public static enum TransitionType {
        RadialExpand, RadialCollapse, FadeIn, FadeOut
    }

    public TransitionLayout(Context context) {
        super(context);
        init(null);
    }

    public TransitionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TransitionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint.setAntiAlias(true);

        internalListener = new DefaultAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                currentView++;
                currentView %= getChildCount();
                animator = null;
            }
        };
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
        startTransition(transitionType, DEFAULT_DURATION);
    }

    public void startTransition(TransitionType transitionType, int duration) {
        switch (transitionType) {
            case RadialExpand:
                startRadialTransition(true, duration);
                inAnimation = true;
                break;
            case RadialCollapse:
                startRadialTransition(false, duration);
                inAnimation = false;
                break;
        }
    }

    private void startRadialTransition(boolean expand, int duration) {
        float dist = FloatMath.sqrt(x * x + y * y);
        dist = Math.max(dist, FloatMath.sqrt((getWidth() - x) * (getWidth() - x) + y * y));
        dist = Math.max(dist, FloatMath.sqrt(x * x + (getHeight() - y) * (getHeight() - y)));
        dist = Math.max(dist, FloatMath.sqrt((getWidth() - x) * (getWidth() - x) + (getHeight() - y) * (getHeight() - y)));
        animator = ValueAnimator.ofFloat(expand ? 0 : dist, expand ? dist : 0);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(internalListener);
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            texture = null;
            texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            textureCanvas = new Canvas(texture);
            paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (animator != null && animator.isRunning() && textureCanvas != null) {
            int outView = inAnimation ? currentView : (currentView + 1) % getChildCount();
            int inView = !inAnimation ? currentView : (currentView + 1) % getChildCount();

            getChildAt(outView).draw(canvas);
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            getChildAt(inView).draw(textureCanvas);

            RectF rect = new RectF();
            rect.bottom = getHeight();
            rect.right = getWidth();
            canvas.drawCircle(x, y, radius, paint);
        } else {
            getChildAt(currentView).draw(canvas);
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
